package vn.com.mbbank.kanban.mbamt.server.service;// Trong file ChangeRequestApprovalResultService.java (Interface)

import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestApprovalResultModel;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing Change Request Approval Results.
 */
public interface ChangeRequestApprovalResultService {
    /**
     * Find Change Request Approval Result by ID.
     *
     * @param id the ID of the Change Request Approval Result
     * @return an Optional containing the Change Request Approval Result if found, or empty if not found
     */
    Optional<ChangeRequestApprovalResultModel> getResultById(Long id);

    /**
     * Find all Change Request Approval Results by Change Request Approval ID.
     *
     * @param changeRequestApprovalId the ID of the Change Request Approval
     * @return a list of Change Request Approval Results associated with the specified Change Request Approval ID
     */
    List<ChangeRequestApprovalResultModel> getResultsByApprovalId(Long changeRequestApprovalId);

    /**
     * Create a new Change Request Approval Result.
     *
     * @param resultModel the Change Request Approval Result model to create
     * @return the created Change Request Approval Result model
     */
    ChangeRequestApprovalResultModel createResult(ChangeRequestApprovalResultModel resultModel);


    /**
     * Update an existing Change Request Approval Result.
     *
     * @param resultModel the Change Request Approval Result model to update
     * @return the updated Change Request Approval Result model
     */
    ChangeRequestApprovalResultModel updateResult(ChangeRequestApprovalResultModel resultModel);

    /**
     * Delete a Change Request Approval Result by ID.
     *
     * @param id the ID of the Change Request Approval Result to delete
     */
    void deleteResult(Long id);
}