package com.example.demo.service;

import com.example.demo.model.BusinessException;
import com.example.demo.model.ChangeRequestRoleUserModel;

import java.util.List;

/**
 * Service interface for managing Change Request Role Users.
 * This service handles the operations related to users assigned to specific
 * roles
 * within a change request.
 */
public interface ChangeRequestRoleUserService {
    /**
     * Save a new Change Request Role User.
     *
     * @param model The model to save
     * @return The saved model
     */
    ChangeRequestRoleUserModel save(ChangeRequestRoleUserModel model);

    /**
     * Find all Change Request Role Users associated with a specific Change Request.
     *
     * @param changeRequestId The ID of the Change Request to find users for.
     *                        Must not be null.  Must be greater than zero.
     * @return List of Change Request Role User models associated with the specified
     */
    List<ChangeRequestRoleUserModel> findAllByChangeRequestId(Long changeRequestId);


    /**
     * Save a list of Change Request Role Users.
     *
     * @param userModels The list of models to save
     */
    void saveAll(List<ChangeRequestRoleUserModel> userModels);

    /**
     * Delete all Change Request Role Users associated with a specific Change Request.
     *
     * @param changeRequestId The ID of the Change Request to delete users for.
     *                        Must not be null.  Must be greater than zero.
     */
    void deleteAllByChangeRequestId(Long changeRequestId);

    /**
     * Validate a list of Change Request Role User models.
     * Checks if required fields are present and if referenced entities exist.
     *
     * @param list The list of Change Request Role User models to validate
     * @throws BusinessException if validation fails
     */
    void validateList(List<ChangeRequestRoleUserModel> list);
}