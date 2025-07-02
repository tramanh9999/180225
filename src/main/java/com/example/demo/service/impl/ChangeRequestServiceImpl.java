package com.example.demo.service.impl;

import com.example.demo.entity.ChangeRequestEntity;
import com.example.demo.enums.ApprovalResultStatus;
import com.example.demo.enums.FlowConstants;
import com.example.demo.enums.NodeCommonType;
import com.example.demo.enums.NodeType;
import com.example.demo.mapper.ChangeRequestMapper;
import com.example.demo.model.*;
import com.example.demo.repository.ChangeRequestRepository;
import com.example.demo.repository.ChangeRequestService;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.example.demo.model.ChangeFlowDataParser.createEdgeMapKey;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChangeRequestServiceImpl implements ChangeRequestService {
    private final ChangeRequestRepository changeRequestRepository;
    private final ChangeRequestMapper changeRequestMapper;
    private final FlowManagementService flowManagementService;
    private final ChangeTemplateService changeTemplateService;
    private final ChangeRequestApprovalService changeRequestApprovalService;
    private final ChangeRequestRoleService changeRequestRoleService;
    private final ChangeRequestHistoryService changeRequestHistoryService;
    private final ChangeStatusService changeStatusService;
    private final MailService mailService;
    private final ChangeRequestApprovalResultService changeRequestApprovalResultService;

    @Override
    public ChangeRequestModel findById(Long changeRequestId) throws BusinessException {
        if (changeRequestId == null) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_ID_REQUIRED);
        }
        ChangeRequestEntity changeRequest = changeRequestRepository.findById(changeRequestId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_ID_NOT_FOUND,
                                changeRequestId));
        return changeRequestMapper.toDto(changeRequest);
    }


    String getUserName() {
        return "kanban_system";

    }

    @Transactional
    @Override
    public ChangeRequestModel processApprovalReply(ChangeRequestApprovalResultModel replyModel) {
        var approvalData = validateAndPrepareApprovalReplyTransition(replyModel);

        var currentChangeFlowEdge = flowManagementService.getOrDefaultEdgeByNodeHandleId(
                approvalData.getCurrentChangeFlowNodeHandleId(), approvalData.getFlowData());
        //update change request approval entity with replyModel
        //throw  business exception if not found change request approval entity

        var changeRequestApproval = approvalData.getChangeRequestApproval();
        changeRequestApproval.setOverallStatus(replyModel.getStatus());
        changeRequestApproval.setOverallUsername(replyModel.getApprovedUser());


        changeRequestApprovalService.save(changeRequestApproval);

        ChangeRequestApprovalResultModel resultModel = ChangeRequestApprovalResultModel.builder()
                .changeRequestApprovalId(replyModel.getChangeRequestApprovalId())
                .approvedUser(changeRequestApproval.getOverallUsername())
                .comment(replyModel.getComment()).status(replyModel.getStatus()).build();
        changeRequestApprovalResultService.createResult(resultModel);

        recursiveProcessChangeRequestTransition(approvalData.getChangeRequest(),
                currentChangeFlowEdge, approvalData.getFlowData());
        return changeRequestMapper.toDto(approvalData.getChangeRequest());
    }

    private FlowTransitionDetail validateAndPrepareApprovalReplyTransition(
            ChangeRequestApprovalResultModel replyModel) {
        if (replyModel == null || replyModel.getStatus() == null) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_APPROVAL_INPUT_REQUIRED);
        }
        var approvalId = replyModel.getChangeRequestApprovalId();
        if (approvalId == null) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_APPROVAL_ID_REQUIRED);
        }
        var approvalModel = changeRequestApprovalService.findById(approvalId);
        if (approvalModel == null) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_APPROVAL_NOT_FOUND,
                    approvalId);
        }

        if (approvalModel.getChangeRequestId() == null) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_APPROVAL_REQUEST_ID_NULL,
                    approvalId);
        }


        var changeRequestId = approvalModel.getChangeRequestId();
        var cr = changeRequestRepository.findById(changeRequestId).orElseThrow(
                () -> new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_ID_NOT_FOUND,
                        changeRequestId));
        var changeTemplateId = cr.getChangeTemplateId();
        if (changeTemplateId == null) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_TEMPLATE_ID_MISSING,
                    changeRequestId);
        }
        var changeTemplateModel = changeTemplateService.findById(changeTemplateId);
        if (changeTemplateModel == null) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_TEMPLATE_NOT_FOUND,
                    changeTemplateId);
        }
        var changeFlowId = changeTemplateModel.getChangeFlowId();
        if (changeFlowId == null) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_FLOW_CONFIGURATION_ERROR,
                    changeTemplateId);
        }

        var indexedChangeFlowData = flowManagementService.getFlowDataByChangeFlowId(changeFlowId);



        /*
         1. Reply with valid role : change request in a change flow node id  = node of change role
        that contain current change approval request id in
         2. Not allow to reply other user's approval request: Config role user is current user
         3. Not allow to reply a approval request which STATUS != REJECTED/ PENDING
         */


        String currentNodeHandleId =
                flowManagementService.buildHandleOutputIdForApprovalAction(cr.getChangeFlowNodeId(),
                        replyModel.getStatus());

        if (!indexedChangeFlowData.getIndexedEdges()
                .containsKey(createEdgeMapKey(currentNodeHandleId))) {
            throw new BusinessException(ErrorCodeCommon.FLOW_TRANSITION_NOT_FOUND,
                    currentNodeHandleId, changeFlowId);
        }
        // get current role of logged user.
        var changeRole =
                changeRequestRoleService.getChangeRequestRoleByChangeFlowNodeId(changeFlowId,
                        changeRequestId, cr.getChangeFlowNodeId());
        return FlowTransitionDetail.builder().changeRequest(cr).changeFlowId(changeFlowId)
                .currentChangeFlowNodeHandleId(currentNodeHandleId).changeRole(changeRole)
                .changeRequestApproval(approvalModel).flowData(indexedChangeFlowData).build();
    }


    @Override
    public FlowTransitionDetail validateAndPrepareChangeCoordinatorTransition(Long changeRequestId,
                                                                              ChangeProcessModel changeProcessModel) {


        long actionOnChangeStatusId = changeProcessModel.getChangeStatusId();
        if (changeRequestId == null) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_ID_REQUIRED);
        }
        ChangeRequestEntity cr = changeRequestRepository.findById(changeRequestId).orElseThrow(
                () -> new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_NOT_FOUND,
                        changeRequestId));
        Long changeTemplateId = cr.getChangeTemplateId();
        if (changeTemplateId == null) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_TEMPLATE_ID_MISSING,
                    changeTemplateId);
        }

        ChangeTemplateModel changeTemplateModel = changeTemplateService.findById(changeTemplateId);
        if (changeTemplateModel == null) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_TEMPLATE_NOT_FOUND,
                    changeTemplateId);
        }

        Long changeFlowId = changeTemplateModel.getChangeFlowId();
        if (changeFlowId == null) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_FLOW_CONFIGURATION_ERROR,
                    changeFlowId);
        }

        String currentChangeFlowNodeHandleId =
                changeProcessModel.getChangeStatusId() + FlowConstants.HANDLE_SEPARATOR +
                        FlowConstants.OUTPUT_KEYWORD; // Append output keyword to change status ID;

        List<ChangeStatusModel> allChangeStatusInSameStage =
                changeStatusService.findAllChangeStatusInSameStage(cr.getChangeStatusId());
        if (allChangeStatusInSameStage == null || allChangeStatusInSameStage.isEmpty()) {
            throw new BusinessException(ErrorCodeCommon.NO_VALID_NEXT_STATUSES_FOUND,
                    cr.getChangeStatusId());
        }

        boolean invalidChangeStatus = allChangeStatusInSameStage.stream()
                .anyMatch(status -> Objects.equals(status.getId(), actionOnChangeStatusId));
        if (!invalidChangeStatus) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_INVALID_UPDATE_STATUS,
                    actionOnChangeStatusId, changeProcessModel.getChangeStatusName(),
                    allChangeStatusInSameStage.get(0).getStage());
        }

        var indexedChangeFlowData = flowManagementService.getFlowDataByChangeFlowId(changeFlowId);
        if (!indexedChangeFlowData.getIndexedEdges()
                .containsKey(createEdgeMapKey(currentChangeFlowNodeHandleId))) {
            throw new BusinessException(ErrorCodeCommon.FLOW_TRANSITION_NOT_FOUND,
                    currentChangeFlowNodeHandleId, changeFlowId);
        }
        return FlowTransitionDetail.builder().changeFlowId(changeFlowId).changeRequest(cr)
                .currentChangeFlowNodeHandleId(currentChangeFlowNodeHandleId)
                .flowData(indexedChangeFlowData).build();
    }


    @Transactional
    @Override
    public ChangeRequestModel processChangeRequestCoordinatorTransition(Long changeRequestId,
                                                                        ChangeProcessModel changeProcessModel) {
        var changeRequestData =
                validateAndPrepareChangeCoordinatorTransition(changeRequestId, changeProcessModel);

        var currentChangeFlowEdge = flowManagementService.getOrDefaultEdgeByNodeHandleId(
                changeRequestData.getCurrentChangeFlowNodeHandleId(),
                changeRequestData.getFlowData());

        if (currentChangeFlowEdge.getTargetNodeModel() == null ||
                currentChangeFlowEdge.getTargetNodeModel().getParsedType() == NodeType.END) {
            // If the next node is not confined in change flow / is END node, then stop processing
            return changeRequestMapper.toDto(changeRequestData.getChangeRequest());
        }


        recursiveProcessChangeRequestTransition(changeRequestData.getChangeRequest(),
                currentChangeFlowEdge, changeRequestData.getFlowData());
        return changeRequestMapper.toDto(changeRequestData.getChangeRequest());
    }


    /**
     * Processes the transition of a Change Request based on detailed flow transition information.
     * Updates change_status_id for stage nodes, creates approval entities for decision nodes,
     * and specifically updates changeFlowNodeHandleId when transitioning to the END node.
     *
     * @param changeRequest The Change Request entity to be processed.
     * @param edgeModel     An object containing details about the current node, next node, and the transition.
     * @throws IllegalArgumentException if Change Request or necessary data is not found.
     */
    @Transactional
    @Override
    public void recursiveProcessChangeRequestTransition(ChangeRequestEntity changeRequest,
                                                        FlowEdgeModel edgeModel,
                                                        IndexedChangeFlowDataModel flowData) {
        Long changeRequestId = changeRequest.getId();
        var nextNode = edgeModel.getTargetNodeModel();
        var currentNode = edgeModel.getSourceNodeModel();
        if (!shouldMoveChangeRequestByCheckingEdge(edgeModel)) {
            return;
        }

        Long nextNodeId = nextNode.getId();
        NodeType nextNodeType = nextNode.getParsedType();

        if (nextNodeType.getCommonType() == NodeCommonType.CHANGE_STAGE) {
            // Case: Next node is a "stage node" , it always connected to a change status
            var newStatusId = edgeModel.getTargetNodeModel().getParsedHandle().getChangeStatusId();

            var approvedAction = currentNode.getParsedHandle().getApprovedAction();

            var historyModel = ChangeRequestHistoryModel.builder().changeRequestId(changeRequestId)
                    .oldChangeStatusId(changeRequest.getChangeStatusId())
                    .newChangeStatusId(newStatusId).oldChangeFlowNodeId(currentNode.getId())
                    .newChangeFlowNodeId(nextNode.getId()).actionTaken(approvedAction).build();
            changeRequestHistoryService.createHistoryRecord(historyModel);

            changeRequest.setChangeStatusId(newStatusId);
            changeRequest.setChangeFlowNodeId(currentNode.getId());
            changeRequestRepository.save(changeRequest);

            //auto process next node handle id if ok
            var nextNodeHandleOutputId = newStatusId + FlowConstants.HANDLE_SEPARATOR +
                    FlowConstants.OUTPUT_KEYWORD; // Append output keyword to new status ID
            var nextNodeHandleOutputEdge =
                    flowManagementService.getOrDefaultEdgeByNodeHandleId(nextNodeHandleOutputId,
                            flowData);
            recursiveProcessChangeRequestTransition(changeRequest, nextNodeHandleOutputEdge,
                    flowData);
        } else if (nextNodeType.getCommonType() == NodeCommonType.CHANGE_ROLE) {

            var nextChangeRole = changeRequestRoleService.getChangeRequestRoleByChangeFlowNodeId(
                    changeRequest.getId(), changeRequest.getChangeTemplateId(),
                    edgeModel.getTargetNodeModel().getId());


            //find at least one approvalModel in nextChangeRole is not ACCEPTED


            //
            List<ChangeRequestApprovalModel> allApprovals = nextChangeRole.getWorkflows().stream()
                    .flatMap(workflow -> workflow.getGroups().stream())
                    .flatMap(group -> group.getUsers().stream())
                    .map(ChangeRequestRoleUserModel::getApprovalModel).toList();
            boolean roleUserNotAcceptedAtAllOrNotCreatedApproval = allApprovals.stream().anyMatch(
                    approval -> approval == null ||
                            approval.getOverallStatus() != ApprovalResultStatus.ACCEPT);


            if (roleUserNotAcceptedAtAllOrNotCreatedApproval) {
                for (ChangeRequestRoleUserWorkflowListModel workflow : nextChangeRole.getWorkflows()) {
                    //flat the users in workflow
                    // and process each group in the workflow
                    List<ChangeRequestRoleUserModel> oneWorkflowUsers =
                            workflow.getGroups().stream()
                                    .flatMap(group -> group.getUsers().stream()).toList();

                    // filter oneWorkflowUsers that has min cabGroup and min cabGroupOrder in a cab group and
                    // approvalModel is null or has status is not ACCEPTED
                    List<ChangeRequestRoleUserModel> filteredUsers = oneWorkflowUsers.stream()
                            .filter(user -> user.getApprovalModel() == null ||
                                    user.getApprovalModel().getOverallStatus() !=
                                            ApprovalResultStatus.ACCEPT)
                            .sorted(Comparator.comparing(ChangeRequestRoleUserModel::getCabGroup)
                                    .thenComparing(ChangeRequestRoleUserModel::getCabGroupOrder))
                            .toList();

                    List<ChangeRequestRoleUserModel> tobeCreateApprovalRequestUsers =
                            filteredUsers.stream().filter(user -> user.getApprovalModel() == null)
                                    .toList();

                    //resend mail to users that has approvalModel is not null and status is REJECTED
                    List<ChangeRequestRoleUserModel> usersToResendMail = filteredUsers.stream()
                            .filter(user -> user.getApprovalModel() != null &&
                                    user.getApprovalModel().getOverallStatus() ==
                                            ApprovalResultStatus.REJECT).toList();

                    if (!usersToResendMail.isEmpty()) {
                        mailService.sendBulkEmail(usersToResendMail, null,
                                "Your approval request has been rejected. Please review and take action.");
                    }


                    handleCreateApprovalRequests(changeRequestId,
                            changeRequest.getChangeTemplateId(), tobeCreateApprovalRequestUsers);


                    ChangeRequestHistoryModel historyModel =
                            ChangeRequestHistoryModel.builder().changeRequestId(changeRequestId)
                                    .oldChangeFlowNodeId(currentNode.getId())
                                    .newChangeFlowNodeId(nextNode.getId()).actionTaken(null)
                                    .build();
                    if (currentNode.getParsedType().getCommonType() ==
                            NodeCommonType.CHANGE_STAGE) {
                        Long nextChangeStatusId = nextNode.getParsedHandle().getChangeStatusId();
                        historyModel.setOldChangeStatusId(changeRequest.getChangeStatusId());
                        historyModel.setNewChangeStatusId(nextChangeStatusId);

                        changeRequest.setChangeStatusId(nextChangeStatusId);
                    }
                    changeRequest.setChangeFlowNodeId(nextNode.getId());
                    changeRequestRepository.save(changeRequest);
                    changeRequestHistoryService.createHistoryRecord(historyModel);

                }
                //stop to wait for approval user take action
                return;
            }


            //auto process next node handle id if ALL role users have ACCEPTED
            var acceptedRoleHistory =
                    ChangeRequestHistoryModel.builder().changeRequestId(changeRequestId)
                            .oldChangeStatusId(changeRequest.getChangeStatusId())
                            .newChangeStatusId(changeRequest.getChangeStatusId())
                            .oldChangeFlowNodeId(currentNode.getId())
                            .newChangeFlowNodeId(nextNodeId)
                            .actionTaken(ApprovalResultStatus.ACCEPT).build();
            changeRequestHistoryService.createHistoryRecord(acceptedRoleHistory);

            //move change request node id to nextNodeId
            changeRequest.setChangeFlowNodeId(nextNodeId);
            changeRequestRepository.save(changeRequest);

            // auto recursiveProcessChangeRequestTransition by Accept output handle
            var followAcceptHandleId =
                    flowManagementService.buildHandleOutputIdForApprovalAction(nextNodeId,
                            ApprovalResultStatus.ACCEPT);
            var followRoleAcceptEdge =
                    flowManagementService.getOrDefaultEdgeByNodeHandleId(followAcceptHandleId,
                            flowData);
            recursiveProcessChangeRequestTransition(changeRequest, followRoleAcceptEdge, flowData);
        } else {
            // Case: Next node is UNKNOWN or any other unexpected type
            log.warn(String.format(
                    "Unhandled transition for Change Request %d. Next node type: %s (ID: %s). No status or approval action taken.",
                    changeRequestId, nextNodeType, nextNodeId));
        }
    }


    boolean shouldMoveChangeRequestByCheckingEdge(FlowEdgeModel edgeModel) {
        // If the target node is END, we should not process further
        boolean shouldMove = edgeModel != null && edgeModel.getTargetNodeModel() != null &&
                NodeType.END != edgeModel.getTargetNodeModel().getParsedType();
        if (!shouldMove) {
            log.info("Change Request will not be moved. Not connect to any node or " +
                    "target node is END node.");
        }
        return shouldMove;

    }

    @Override
    public void handleCreateApprovalRequests(Long changeRequestId, Long changeTemplateId,
                                             List<ChangeRequestRoleUserModel> roleUserList) {


        if (roleUserList == null || roleUserList.isEmpty()) {
            return;
        }

        List<ChangeRequestApprovalModel> approvalList = roleUserList.stream()
                .map(user -> ChangeRequestApprovalModel.builder().changeRequestId(changeRequestId)
                        .overallStatus(ApprovalResultStatus.PENDING_APPROVAL)
                        .changeRequestRoleUserId(user.getId()).build()).toList();
        changeRequestApprovalService.saveList(approvalList);
    }
}