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
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.service.impl.FlowManagementServiceImpl.createEdgeMapKey;


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
        changeRequestApproval.setOverallUsername(getUserName());

        changeRequestApprovalService.save(changeRequestApproval);

        ChangeRequestApprovalResultModel historyApproval =
                ChangeRequestApprovalResultModel.builder()
                        .changeRequestApprovalId(replyModel.getChangeRequestApprovalId())
                        .approvedUser(changeRequestApproval.getOverallUsername())
                        .replyComment(replyModel.getReplyComment()).status(replyModel.getStatus())
                        .build();
        changeRequestApprovalResultService.createResult(historyApproval);

        log.info("Processing approval reply for Change Request ID: {} with status: {}",
                approvalData.getChangeRequest().getId(), replyModel.getStatus());


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
        var changeRole =
                changeRequestRoleService.getChangeRequestRoleByChangeFlowNodeId(changeRequestId,
                        cr.getChangeTemplateId(), cr.getChangeFlowNodeId());

        //filter the indexed nodes to find the current change flow node

        var currentNodeStream = indexedChangeFlowData.getIndexedNodes().values().stream()
                .filter(node -> Objects.equals(node.getId(), cr.getChangeFlowNodeId())).findFirst()
                .orElseThrow(
                        () -> new BusinessException(ErrorCodeCommon.CHANGE_FLOW_NODES_NOT_FOUND,
                                cr.getChangeFlowNodeNodeId()));

        String currentNodeHandleId = flowManagementService.buildHandleOutputIdForApprovalAction(
                currentNodeStream.getNodeId(), replyModel.getStatus());

        if (!indexedChangeFlowData.getIndexedEdges()
                .containsKey(createEdgeMapKey(currentNodeHandleId))) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_APPROVAL_UNREPLIABLE,
                    replyModel.getStatus(), approvalId, changeRole.getChangeFlowNode().getName());
        }




        /*
         1. Reply with valid role : change request in a change flow node id  = node of change role
        that contain current change approval request id in
         2. Not allow to reply other user's approval request: Config role user is current user
         3. Not allow to reply a approval request which STATUS != REJECTED/ PENDING
         */


        // get current role of logged user.

        String currentUser = getUserName();

        //get current change request role user by current user and
        // previous change role user that
        // has same or less cabGroup and cabGroupOrder


        return FlowTransitionDetail.builder().changeRequest(cr).changeFlowId(changeFlowId)
                .currentChangeFlowNodeHandleId(currentNodeHandleId).changeRole(changeRole)
                .changeRequestApproval(approvalModel).flowData(indexedChangeFlowData).build();
    }


    /**
     * Filters a list of WorkflowUsers (assumed to be sorted by userOrder)
     * to find records with REJECT or PENDING status that are:
     * 1. After the latest ACCEPT record.
     * 2. Before the earliest NULL_STATUS record.
     *
     * @param sortedUsersInGroup A list of WorkflowUser objects, already sorted by userOrder.
     * @return A list of WorkflowUser objects matching the criteria.
     */
    public List<ChangeRequestRoleUserModel> filterPendingOrRejectInWindow(
            List<ChangeRequestRoleUserModel> sortedUsersInGroup) {
        if (sortedUsersInGroup == null || sortedUsersInGroup.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        // Find the userOrder of the latest ACCEPT record
        // We initialize with -1 to ensure any valid order (0 or positive) will be greater
        int latestAcceptOrder = -1;
        Optional<ChangeRequestRoleUserModel> latestAcceptUser = sortedUsersInGroup.stream()
                .filter(user -> user.getApprovalModel() != null &&
                        ApprovalResultStatus.ACCEPT.equals(
                                user.getApprovalModel().getOverallStatus()))
                .max(Comparator.comparingInt(
                        ChangeRequestRoleUserModel::getCabGroupOrder)); // Find max
        // order among ACCEPTS

        if (latestAcceptUser.isPresent()) {
            latestAcceptOrder = latestAcceptUser.get().getCabGroupOrder();
        }

        // Find the userOrder of the earliest NULL_STATUS record
        // We initialize with Integer.MAX_VALUE to ensure any valid order will be smaller
        int earliestNullStatusOrder = Integer.MAX_VALUE;
        Optional<ChangeRequestRoleUserModel> earliestNullStatusUser =
                sortedUsersInGroup.stream().filter(user -> user.getApprovalModel() == null)
                        .min(Comparator.comparingInt(
                                ChangeRequestRoleUserModel::getCabGroupOrder)); // Find min
        // order among NULL_STATUS

        if (earliestNullStatusUser.isPresent()) {
            earliestNullStatusOrder = earliestNullStatusUser.get().getCabGroupOrder();
        }

        // Filter users that are REJECT or PENDING and fall within the defined window
        final int finalLatestAcceptOrder = latestAcceptOrder;
        final int finalEarliestNullStatusOrder = earliestNullStatusOrder;

        return sortedUsersInGroup.stream().filter(user -> (user.getApprovalModel() != null &&
                        (ApprovalResultStatus.REJECT.equals(user.getApprovalModel().getOverallStatus()) ||
                                ApprovalResultStatus.PENDING_APPROVAL.equals(
                                        user.getApprovalModel().getOverallStatus()))))
                .filter(user -> user.getCabGroupOrder() >
                        finalLatestAcceptOrder) // Must be after the
                // latest ACCEPT
                .filter(user -> user.getCabGroupOrder() <
                        finalEarliestNullStatusOrder) // Must be before the earliest NULL_STATUS
                .collect(Collectors.toList());
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
            throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_APPROVAL_UNREPLIABLE,
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
        ApprovalResultStatus actionTaken = null;
        Long nextChangeStatusId = changeProcessModel.getChangeStatusId();
        transitionChangeRequest(changeRequestData.getChangeRequest(), nextChangeStatusId,
                changeRequestData.getChangeRequest().getChangeFlowNodeId(), actionTaken);

        recursiveProcessChangeRequestTransition(changeRequestData.getChangeRequest(),
                currentChangeFlowEdge, changeRequestData.getFlowData());
        return changeRequestMapper.toDto(changeRequestData.getChangeRequest());
    }


    @Transactional
    @Override
    public void continueProcessStageNode(ChangeRequestEntity changeRequest,
                                         ChangeFlowNodeModel previousNode,
                                         ChangeFlowNodeModel currentNode,
                                         IndexedChangeFlowDataModel flowData) {
        // Case: Next node is a "stage node" , it always connected to a change status
        var currentStatusId = currentNode.getParsedHandle().getChangeStatusId();
        //auto process next node handle id if ok
        var currentOutputHandleId =
                currentStatusId + FlowConstants.HANDLE_SEPARATOR + FlowConstants.OUTPUT_KEYWORD;
        var currentEdge =
                flowManagementService.getOrDefaultEdgeByNodeHandleId(currentOutputHandleId,
                        flowData);
        recursiveProcessChangeRequestTransition(changeRequest, currentEdge, flowData);
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
        //check null nextnode
        if (!shouldMoveChangeRequestByCheckingEdge(edgeModel)) {
            return;
        }
        var previousNode = edgeModel.getSourceNodeModel();
        var currentNode = edgeModel.getTargetNodeModel();
        Long currentNodeId = currentNode.getId();
        NodeType currentNodeType = currentNode.getParsedType();

        var actionTaken = previousNode.getParsedHandle().getApprovedAction();
        Long nextChangeStatusId =
                currentNode.getParsedType().getCommonType() == NodeCommonType.CHANGE_STAGE ?
                        currentNode.getParsedHandle().getChangeStatusId() :
                        changeRequest.getChangeStatusId();
        transitionChangeRequest(changeRequest, nextChangeStatusId, currentNodeId, actionTaken);

        if (currentNodeType.getCommonType() == NodeCommonType.CHANGE_STAGE) {
            continueProcessStageNode(changeRequest, previousNode, currentNode, flowData);
        } else if (currentNodeType.getCommonType() == NodeCommonType.CHANGE_ROLE) {
            var currentChangeRole = changeRequestRoleService.getChangeRequestRoleByChangeFlowNodeId(
                    changeRequest.getId(), changeRequest.getChangeTemplateId(),
                    currentNode.getId());
            //add logic to check if empty users in currentChangeRole
            var approvalList = currentChangeRole.getWorkflows().stream()
                    .flatMap(workflow -> workflow.getGroups().stream())
                    .flatMap(group -> group.getUsers().stream())
                    .map(ChangeRequestRoleUserModel::getApprovalModel).toList();
            //check approvalList empty or all users accepted

            boolean allUserAccepted = !approvalList.isEmpty() && approvalList.stream().noneMatch(
                    approval -> approval == null || !Objects.equals(ApprovalResultStatus.ACCEPT,
                            approval.getOverallStatus()));
            if (allUserAccepted) {
                continueProcessAcceptedApprovalNode(changeRequest, previousNode, currentNode,
                        flowData);
            } else {
                continueProcessApprovalNode(changeRequest, previousNode, currentNode,
                        currentChangeRole);
            }
        } else {
            // Case: Next node is UNKNOWN or any other unexpected type
            log.warn(String.format(
                    "Unhandled transition for Change Request %d. Next node type: %s (ID: %s). No status or approval action taken.",
                    changeRequestId, currentNodeType, currentNodeId));

        }


    }


    @Override
    @Transactional
    public void continueProcessAcceptedApprovalNode(ChangeRequestEntity changeRequest,
                                                    ChangeFlowNodeModel previousNode,
                                                    ChangeFlowNodeModel currentNode,
                                                    IndexedChangeFlowDataModel flowData) {
        Long currentNodeId = currentNode.getId();
        // auto recursiveProcessChangeRequestTransition by Accept output handle

        var currentNodeStream = flowData.getIndexedNodes().values().stream()
                .filter(node -> Objects.equals(node.getId(), currentNodeId)).findFirst()
                .orElseThrow(
                        () -> new BusinessException(ErrorCodeCommon.CHANGE_FLOW_NODES_NOT_FOUND,
                                currentNodeId));

        String currentAcceptHandleOutputId =
                flowManagementService.buildHandleOutputIdForApprovalAction(
                        currentNodeStream.getNodeId(), ApprovalResultStatus.ACCEPT);


        var currentEdge =
                flowManagementService.getOrDefaultEdgeByNodeHandleId(currentAcceptHandleOutputId,
                        flowData);
        recursiveProcessChangeRequestTransition(changeRequest, currentEdge, flowData);
    }

    private void continueProcessApprovalNode(ChangeRequestEntity changeRequest,
                                             ChangeFlowNodeModel previousNode,
                                             ChangeFlowNodeModel currentNode,
                                             ChangeRequestRoleModel currentRole) {

        var actionTaken = previousNode.getParsedHandle().getApprovedAction();
        transitionChangeRequest(changeRequest, changeRequest.getChangeStatusId(),
                currentNode.getId(), actionTaken);
        for (ChangeRequestRoleUserWorkflowListModel oneWorkflowWithApprovalData : currentRole.getWorkflows()) {
            //flat the users in workflow
            // and process each group in the workflow
            List<ChangeRequestRoleUserModel> oneWorkflowUsers =
                    oneWorkflowWithApprovalData.getGroups().stream()
                            .flatMap(group -> group.getUsers().stream()).toList();

            // filter oneWorkflowUsers that has min cabGroup and min cabGroupOrder in a cab group and
            // approvalModel is null or has status is not ACCEPTED
            List<ChangeRequestRoleUserModel> filteredUsers = oneWorkflowUsers.stream()
                    .filter(user -> user.getApprovalModel() == null ||
                            user.getApprovalModel().getOverallStatus() !=
                                    ApprovalResultStatus.ACCEPT)
                    .sorted(Comparator.comparing(ChangeRequestRoleUserModel::getCabGroup)
                            .thenComparing(ChangeRequestRoleUserModel::getCabGroupOrder)).toList();

            List<ChangeRequestRoleUserModel> tobeCreateApprovalRequestUsers =
                    filteredUsers.stream().filter(user -> user.getApprovalModel() == null).toList();

            //resend mail to users that has approvalModel is not null and status is REJECTED
            List<ChangeRequestRoleUserModel> usersToResendMail = filteredUsers.stream()
                    .filter(user -> user.getApprovalModel() != null &&
                            user.getApprovalModel().getOverallStatus() ==
                                    ApprovalResultStatus.REJECT).toList();


            if (!usersToResendMail.isEmpty()) {
                //log the users to resend mail
                log.info("Resending mail to users: " +
                        usersToResendMail.stream().map(ChangeRequestRoleUserModel::getUsername)
                                .toList());
                mailService.sendBulkEmail(usersToResendMail, null,
                        "Your approval request has been rejected. Please review and take action.");
            }


            //log the users to create approval request
            log.info("Creating approval requests for users: " +
                    tobeCreateApprovalRequestUsers.stream()
                            .map(ChangeRequestRoleUserModel::getUsername).toList());

            if (!tobeCreateApprovalRequestUsers.isEmpty()) {
                handleCreateApprovalRequests(changeRequest.getId(),
                        changeRequest.getChangeTemplateId(), tobeCreateApprovalRequestUsers);
            }
        }
        // maybe save info accepted , rejected, resend mail in change request here
    }

    void transitionChangeRequest(ChangeRequestEntity changeRequest, Long newChangeRequestStatusId,
                                 Long newChangeFlowNodeId, ApprovalResultStatus actionTaken) {


        //log change request
        log.info(String.format(
                "Change Request %d moved from node %s -> %s with status %d -> " + "%d",
                changeRequest.getId(), changeRequest.getChangeFlowNodeId(), newChangeFlowNodeId,
                changeRequest.getChangeStatusId(), newChangeRequestStatusId));
        // Update the change request with new status and node id if at lease modified
        if (!Objects.equals(changeRequest.getChangeStatusId(), newChangeRequestStatusId) ||
                !Objects.equals(changeRequest.getChangeFlowNodeId(), newChangeFlowNodeId)) {
            changeRequest.setChangeStatusId(newChangeRequestStatusId);
            changeRequest.setChangeFlowNodeId(newChangeFlowNodeId);
            changeRequestRepository.save(changeRequest);

            //add logic save change request history
            ChangeRequestHistoryModel historyModel =
                    ChangeRequestHistoryModel.builder().changeRequestId(changeRequest.getId())
                            .oldChangeStatusId(changeRequest.getChangeStatusId())
                            .newChangeStatusId(newChangeRequestStatusId)
                            .oldChangeFlowNodeId(changeRequest.getChangeFlowNodeId())
                            .newChangeFlowNodeId(newChangeFlowNodeId).actionTaken(actionTaken)
                            .build();
            changeRequestHistoryService.save(historyModel);
        }
    }


    boolean shouldMoveChangeRequestByCheckingEdge(FlowEdgeModel edgeModel) {
        // If the target node is END, we should not process further
        boolean shouldMove = edgeModel != null && edgeModel.getTargetNodeModel() != null &&
                NodeType.END != edgeModel.getTargetNodeModel().getParsedType();
        if (!shouldMove) {
            log.info("Change Request will not be moved. next node is " + edgeModel);
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
        // lich su

    }
}