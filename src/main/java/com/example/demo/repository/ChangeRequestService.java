package com.example.demo.repository;

import com.example.demo.entity.ChangeRequestEntity;
import com.example.demo.model.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
    ChangeRequestApprovalResultModel processApprovalReply(
            ChangeRequestApprovalResultModel replyModel);


    /**
     * Validate and prepare transition details flow transition details.
     *
     * @param changeRequestId    the change request id
     * @param nextChangeStatusId the next change status id
     * @return the flow transition details
     */
    FlowTransitionDetail validateAndPrepareChangeCoordinatorTransition(Long changeRequestId,
                                                                       Long nextChangeStatusId);

    /**
     * Process change request coordinator transition change request model.
     *
     * @param changeRequestId the change request id
     * @param changeStatusId
     * @return the updated ChangeRequestModel after processing the coordinator transition
     * @throws BusinessException        if the change request is not found or cannot be processed
     * @throws IllegalStateException    if the change request is not in a state that allows transition
     * @throws IllegalArgumentException if the change request id is null or invalid
     * @throws RuntimeException         for any unexpected errors during processing
     */
    @Transactional
    ChangeRequestModel processChangeRequestCoordinatorTransition(Long changeRequestId,
                                                                 Long changeStatusId);


    @Transactional
    void recursiveProcessChangeRequestTransition(ChangeRequestEntity changeRequest,
                                                 FlowEdgeModel transitionDetails,
                                                 Map<String, FlowEdgeModel> indexedEdges);

    void handleCreateApprovalRequests(Long changeRequestId, Long changeTemplateId);

    List<ChangeRequestApprovalModel> createApprovalRequestByChangeRoles(
            List<ChangeRequestRoleModel> changeRequestRoles);
}
