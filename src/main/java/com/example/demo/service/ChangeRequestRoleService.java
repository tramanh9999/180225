package com.example.demo.service;

import com.example.demo.model.BusinessException;
import com.example.demo.model.ChangeRequestRoleModel;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Service interface for managing Change Request Roles.
 */
public interface ChangeRequestRoleService {
    @Transactional
    void saveList(Long changeRequestId, List<ChangeRequestRoleModel> roles);

    /**
     * Find all Change Request Roles associated with a specific Change Request.
     *
     * @param changeRequestId The ID of the Change Request
     * @return List of Change Request Role entities associated with the specified
     * Change Request ID
     */
    List<ChangeRequestRoleModel> findAllByChangeRequestId(Long changeRequestId);

    /**
     * Validate a list of Change Request Role models.
     * Checks if required fields are present and if referenced entities exist.
     *
     * @param roles The list of Change Request Role models to validate
     * @throws BusinessException if validation fails
     */
    void validateList(List<ChangeRequestRoleModel> roles);

    List<ChangeRequestRoleModel> findAllChangeFlowNodesByChangeTemplateIdOrRequestId(
            Long changeTemplateId, Long changeRequestId) throws BusinessException;

    /**
     * Group and sort Change Request Roles by user groups.
     * This method groups the roles by user groups and sorts them within each group.
     *
     * @param items The list of Change Request Role models to group and sort
     * @return
     */
    List<ChangeRequestRoleModel> groupAndSortCabUserGroups2(List<ChangeRequestRoleModel> items);
}
