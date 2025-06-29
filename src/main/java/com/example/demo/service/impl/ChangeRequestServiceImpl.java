package com.example.demo.service.impl;

import com.example.demo.entity.ChangeRequestEntity;
import com.example.demo.enums.*;
import com.example.demo.mapper.ChangeRequestMapper;
import com.example.demo.model.*;
import com.example.demo.repository.ChangeRequestRepository;
import com.example.demo.repository.ChangeRequestService;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private final ChangeFlowService changeFlowService;

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


    @Transactional
    @Override
    public ChangeRequestApprovalResultModel processApprovalReply(
            ChangeRequestApprovalResultModel replyModel) {
        var flowTransitionDetails = validateAndPrepareApprovalReplyTransition(replyModel);
        var flowData = flowManagementService.getFlowDataByChangeFlowId(
                flowTransitionDetails.getChangeFlowId());
        var indexedEdges = flowData.getIndexedEdges();
        var transitionDetails = flowManagementService.getOrDefaultEdgeByNodeHandleId(
                flowTransitionDetails.getChangeFlowId(),
                flowTransitionDetails.getCurrentChangeFlowNodeHandleId(), indexedEdges);
        transitionDetails.setResultStatus(replyModel.getStatus());
        recursiveProcessChangeRequestTransition(flowTransitionDetails.getChangeRequest(),
                transitionDetails, indexedEdges);
        return replyModel;
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

        changeFlowService.findById(changeFlowId).orElseThrow(
                () -> new BusinessException(ErrorCodeCommon.CHANGE_FLOW_NOT_FOUND, changeFlowId));

        String currentNodeHandleId =
                flowManagementService.createApprovalNodeHandleIdFromNodeIdAndStatus(
                        cr.getChangeFlowNodeStrId(), replyModel.getStatus());

        return FlowTransitionDetail.builder().changeRequest(cr).changeFlowId(changeFlowId)
                .currentChangeFlowNodeHandleId(currentNodeHandleId).build();
    }


    @Override
    public FlowTransitionDetail validateAndPrepareChangeCoordinatorTransition(Long changeRequestId,
                                                                              Long nextChangeStatusId) {
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
        Long currentChangeStatusId = cr.getChangeStatusId();
        String currentChangeFlowNodeHandleId = FlowConstants.START_NODE_SOURCE_HANDLE_ID;
        if (currentChangeStatusId != null) {
            currentChangeFlowNodeHandleId = currentChangeStatusId + FlowConstants.HANDLE_SEPARATOR +
                    FlowConstants.OUTPUT_KEYWORD;
        }

        List<ChangeStatusModel> remainingStatusesInSameStage =
                changeStatusService.findRemainingStatusesInSameStage(currentChangeStatusId);
        if (remainingStatusesInSameStage == null || remainingStatusesInSameStage.isEmpty()) {
            throw new BusinessException(ErrorCodeCommon.NO_VALID_NEXT_STATUSES_FOUND,
                    currentChangeStatusId);
        }

        if (nextChangeStatusId == null) {
            throw new BusinessException(ErrorCodeCommon.INVALID_CHANGE_STATUS);
        }
        boolean isValidNextStatus = remainingStatusesInSameStage.stream()
                .anyMatch(status -> Objects.equals(status.getId(), nextChangeStatusId));
        if (!isValidNextStatus) {
            throw new BusinessException(ErrorCodeCommon.NEXT_STATUS_NOT_IN_STAGE,
                    nextChangeStatusId, currentChangeStatusId);
        }

        return FlowTransitionDetail.builder().changeFlowId(changeFlowId).changeRequest(cr)
                .currentChangeFlowNodeHandleId(currentChangeFlowNodeHandleId).build();
    }


    @Transactional
    @Override
    public ChangeRequestModel processChangeRequestCoordinatorTransition(Long changeRequestId,
                                                                        Long nextChangeStatusId) {

        var details =
                validateAndPrepareChangeCoordinatorTransition(changeRequestId, nextChangeStatusId);
        IndexedChangeFlowDataModel flowData =
                flowManagementService.getFlowDataByChangeFlowId(details.getChangeFlowId());
        Map<String, FlowEdgeModel> indexedEdges = flowData.getIndexedEdges();
        FlowEdgeModel transitionDetails =
                flowManagementService.getOrDefaultEdgeByNodeHandleId(details.getChangeFlowId(),
                        details.getCurrentChangeFlowNodeHandleId(), indexedEdges);
        recursiveProcessChangeRequestTransition(details.getChangeRequest(), transitionDetails,
                indexedEdges);
        return changeRequestMapper.toDto(details.getChangeRequest());
    }


    /**
     * Processes the transition of a Change Request based on detailed flow transition information.
     * Updates change_status_id for stage nodes, creates approval entities for decision nodes,
     * and specifically updates changeFlowNodeHandleId when transitioning to the END node.
     *
     * @param changeRequest     The Change Request entity to be processed.
     * @param transitionDetails An object containing details about the current node, next node, and the transition.
     * @throws IllegalArgumentException if Change Request or necessary data is not found.
     */
    @Transactional
    @Override
    public void recursiveProcessChangeRequestTransition(ChangeRequestEntity changeRequest,
                                                        FlowEdgeModel transitionDetails,
                                                        Map<String, FlowEdgeModel> indexedEdges) {
        if (transitionDetails == null) {
            throw new IllegalArgumentException("Transition details must not be null");
        }
        Long changeRequestId = changeRequest.getId();
        String nextNodeId;
        NodeType nextFlowNodeType;
        boolean hasNextNodeId = transitionDetails.getTargetNode() != null;
        if (!hasNextNodeId) {
            handleStopTransitionAndUpdateChange(changeRequestId, changeRequest, transitionDetails);
            return;
        }
        nextNodeId = transitionDetails.getTargetNode().getId();
        nextFlowNodeType = transitionDetails.getTargetNode().getType();
        String currentHandleId = transitionDetails.getSourceHandle().getRawHandleId();
        boolean nextTargetIsEndNode = (nextFlowNodeType == NodeType.END);
        if (nextTargetIsEndNode) {
            handleStopTransitionAndUpdateChange(changeRequestId, changeRequest, transitionDetails);
        } else if (nextFlowNodeType.getCommonType() == NodeCommonType.CHANGE_STAGE) {
            // Case: Next node is a "stage node"
            Long newStatusId = transitionDetails.getTargetHandle().getChangeStatusId();

            if (newStatusId != null) {
                ChangeRequestHistoryModel historyModel =
                        ChangeRequestHistoryModel.builder().changeRequestId(changeRequestId)
                                .oldChangeStatusId(changeRequest.getChangeStatusId())
                                .newChangeStatusId(newStatusId) // Get updated status ID after
                                .oldChangeFlowNodeHandleId(changeRequest.getChangeFlowNodeStrId())
                                .newChangeFlowNodeHandleId(transitionDetails.getTargetNodeId())
                                .actionTaken(transitionDetails.getSourceHandle().getCustomAction())
                                .build();

                changeRequestHistoryService.createHistoryRecord(historyModel);

                String nextHandleId = newStatusId + FlowConstants.HANDLE_SEPARATOR +
                        FlowConstants.OUTPUT_KEYWORD; // Append output keyword to new status ID


                FlowEdgeModel nextStepTransitionDetails =
                        flowManagementService.getOrDefaultEdgeByNodeHandleId(
                                transitionDetails.getChangeFlowId(), nextHandleId, indexedEdges);


                recursiveProcessChangeRequestTransition(changeRequest, nextStepTransitionDetails,
                        indexedEdges);
            } else {
                System.err.println(String.format(
                        "Warning: Next node %s is a stage node, but no new status ID was provided in transition details for Change Request %d. Status not updated.",
                        nextNodeId, changeRequestId));
            }
        } else if (nextFlowNodeType.getCommonType() == NodeCommonType.CHANGE_ROLE) {
            // Case: Next node is an "approval/CAB node"
            changeRequest.setChangeFlowNodeStrId(currentHandleId);

            ChangeRequestHistoryModel historyModel =
                    ChangeRequestHistoryModel.builder().changeRequestId(changeRequestId)
                            .oldChangeStatusId(changeRequest.getChangeStatusId()).newChangeStatusId(
                                    transitionDetails.getTargetHandle().getChangeStatusId())
                            .oldChangeFlowNodeHandleId(changeRequest.getChangeFlowNodeStrId())
                            .newChangeFlowNodeHandleId(transitionDetails.getTargetNodeId())
                            .actionTaken(transitionDetails.getSourceHandle().getCustomAction())
                            .build();

            changeRequestHistoryService.createHistoryRecord(historyModel);
            changeRequestRepository.save(
                    changeRequest); // Save the handle ID before creating approval
            handleCreateApprovalRequests(changeRequestId, changeRequest.getChangeTemplateId());
        } else {
            // Case: Next node is UNKNOWN or any other unexpected type
            System.err.println(String.format(
                    "Unhandled transition for Change Request %d. Next node type: %s (ID: %s). No status or approval action taken.",
                    changeRequestId, nextFlowNodeType, nextNodeId));
        }
    }

    private void handleStopTransitionAndUpdateChange(Long changeRequestId,
                                                     ChangeRequestEntity changeRequest,
                                                     FlowEdgeModel transitionDetails) {
        // chờ đóng change
        //changeRequest.setChangeFlowNodeId(transitionDetails.getC;
        //changeRequest.setChangeStatusId(transitionDetails.getCurrentStatusId());
        // changeStatusId remains unchanged as per requirement
        changeRequestRepository.save(changeRequest);
        System.out.println(String.format(
                "No more transition, Change Request %d to be updated changeFlowNodeHandleId to " +
                        "'%s'.", changeRequestId, transitionDetails.getTargetNodeId()));
    }

    @Override
    public void handleCreateApprovalRequests(Long changeRequestId, Long changeTemplateId) {
        var roles = changeRequestRoleService.findAllChangeFlowNodesByChangeTemplateIdOrRequestId(
                changeTemplateId, changeRequestId);
        changeRequestApprovalService.saveList(createApprovalRequestByChangeRoles(roles));
    }


    /**
     * Converts a list of ChangeRequestRoleModel (containing nested user groups)
     * into a flat list of ChangeRequestApprovalModel using Java Streams.
     * Each ApprovalModel represents an individual approval task for a ChangeRequestRoleUserModel.
     *
     * @param changeRequestRoles A list of ChangeRequestRoleModel.
     * @return A flattened list of ChangeRequestApprovalModel objects, or an empty list if input is null or empty.
     */
    @Override
    public List<ChangeRequestApprovalModel> createApprovalRequestByChangeRoles(
            List<ChangeRequestRoleModel> changeRequestRoles) {

        if (changeRequestRoles == null || changeRequestRoles.isEmpty()) {
            return new ArrayList<>();
        }

        return changeRequestRoles.stream().filter(roleModel -> roleModel.getWorkflowUsers() != null)
                .flatMap(roleModel -> {
                    Long currentChangeRequestId = roleModel.getChangeRequestId();
                    ChangeFlowNodeType roleType = (roleModel.getChangeFlowNode() != null) ?
                            roleModel.getChangeFlowNode().getType() : null;

                    return roleModel.getWorkflowUsers().stream()
                            .filter(workflowUserList -> workflowUserList.getWorkflows() != null)
                            .flatMap(workflowUserList -> workflowUserList.getWorkflows().stream()
                                    .filter(userList -> userList.getUsers() != null &&
                                            !userList.getUsers().isEmpty()).flatMap(userList -> {
                                        List<ChangeRequestRoleUserModel> usersToApprove =
                                                new ArrayList<>();
                                        if (ChangeFlowNodeType.CAB.equals(roleType)) {
                                            usersToApprove.add(userList.getUsers().get(0));
                                        } else {
                                            usersToApprove.addAll(userList.getUsers());
                                        }
                                        return usersToApprove.stream().map(user -> {
                                            ChangeRequestApprovalModel approvalModel =
                                                    new ChangeRequestApprovalModel();
                                            approvalModel.setChangeRequestId(
                                                    currentChangeRequestId);
                                            approvalModel.setChangeRequestRoleUserId(user.getId());
                                            approvalModel.setOverallStatus(
                                                    ApprovalResultStatus.PENDING_APPROVAL);
                                            return approvalModel;
                                        });
                                    }));
                }).collect(Collectors.toList());
    }

}