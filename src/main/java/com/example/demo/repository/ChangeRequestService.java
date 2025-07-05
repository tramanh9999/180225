package com.example.demo.repository;

import com.example.demo.entity.ChangeRequestEntity;
import com.example.demo.model.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service interface for ChangeRequestModel.
 */
public interface ChangeRequestService {

    /**
     * Find by id change request model.
     *
     * @param id the id
     * @return the change request model
     * @throws BusinessException the business exception
     */
    ChangeRequestModel findById(Long id) throws BusinessException;


    /**
     * Process approval reply change request approval result model.
     *
     * @param replyModel the reply model
     * @return the change request approval result model
     */
    @Transactional
    ChangeRequestModel processChangeRequestApprovalReply(
            ChangeRequestApprovalResultModel replyModel);


    /**
     * Validate and prepare transition details flow transition details.
     *
     * @param changeRequestId        the change request id
     * @param actionOnChangeStatusId the next change status id
     * @return the flow transition details
     */
    FlowTransitionDetail validateAndPrepareChangeCoordinatorTransition(Long changeRequestId,
                                                                       ChangeProcessModel actionOnChangeStatusId);

    /**
     * Process change request coordinator transition change request model.
     *
     * @param changeRequestId the change request id
     * @param changeStatusId  changeStatusId
     * @return the updated ChangeRequestModel after processing the coordinator transition
     * @throws BusinessException        if the change request is not found or cannot be processed
     * @throws IllegalStateException    if the change request is not in a state that allows transition
     * @throws IllegalArgumentException if the change request id is null or invalid
     * @throws RuntimeException         for any unexpected errors during processing
     */
    @Transactional
    ChangeRequestModel processChangeRequestCoordinatorTransition(Long changeRequestId,
                                                                 ChangeProcessModel changeStatusId);


    /**
     * Continue process stage node.
     *
     * @param changeRequest the change request
     * @param currentNode   the current node
     * @param nextNode      the next node
     * @param flowData      the flow data
     */
    @Transactional
    void continueProcessStageNode(ChangeRequestEntity changeRequest,
                                  ChangeFlowNodeModel currentNode, ChangeFlowNodeModel nextNode,
                                  IndexedChangeFlowDataModel flowData);

    /**
     * Recursive process change request transition.
     *
     * @param changeRequest the change request
     * @param edgeModel     the edge model
     * @param flowData      the flow data
     */
    @Transactional
    void recursiveProcessChangeRequestTransition(ChangeRequestEntity changeRequest,
                                                 FlowEdgeModel edgeModel,
                                                 IndexedChangeFlowDataModel flowData);


    /**
     * Continue process accepted approval node.
     *
     * @param changeRequest the change request
     * @param currentNode   the current node
     * @param nextNode      the next node
     * @param flowData      the flow data
     */
    void continueProcessAcceptedApprovalNode(ChangeRequestEntity changeRequest,
                                             ChangeFlowNodeModel currentNode,
                                             ChangeFlowNodeModel nextNode,
                                             IndexedChangeFlowDataModel flowData);

    /**
     * Handle create approval requests.
     *
     * @param changeRequestId                            the change request id
     * @param changeTemplateId                           the change template id
     * @param remainTobeCreateApprovalRequestForRoleUser the remain tobe create approval request for role user
     */
    void handleCreateApprovalRequests(Long changeRequestId, Long changeTemplateId,
                                      List<ChangeRequestRoleUserModel> remainTobeCreateApprovalRequestForRoleUser);

}
