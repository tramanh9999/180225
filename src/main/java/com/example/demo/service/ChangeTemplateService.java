package com.example.demo.service;

import com.example.demo.model.ChangeTemplateFieldItemModel;
import com.example.demo.model.ChangeTemplateModel;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service interface for managing Change Templates.
 */
public interface ChangeTemplateService {
    /**
     * Finds paginated Change Templates based on the provided request.
     *
     * @param paginationRequest The pagination request object.
     * @return A page of ChangeTemplateModel.
     */
    Page<ChangeTemplateModel> findPagings(Object paginationRequest);

    /**
     * Deletes Change Templates by their IDs.
     *
     * @param ids The list of IDs to delete.
     */
    void deleteByIds(List<Long> ids);

    /**
     * Finds a Change Template by its ID.
     *
     * @param id The ID of the Change Template.
     * @return The ChangeTemplateModel found, or null if not found.
     */
    ChangeTemplateModel findById(Long id);

    /**
     * Saves a new Change Template.
     *
     * @param changeTemplateModel The ChangeTemplateModel to save.
     * @return The saved ChangeTemplateModel.
     */
    ChangeTemplateModel save(ChangeTemplateModel changeTemplateModel);

    /**
     * Saves an existing Change Template by ID.
     *
     * @param id                  The ID of the Change Template to update.
     * @param changeTemplateModel The updated ChangeTemplateModel.
     * @return The saved ChangeTemplateModel.
     */
    ChangeTemplateModel save(Long id, ChangeTemplateModel changeTemplateModel);

    /**
     * Gets paginated field item data for a Change Template.
     *
     * @param changeTemplateId The ID of the Change Template.
     * @param page             The page number (0-indexed).
     * @param size             The number of items per page.
     * @return A page of field item data.
     */
    Page<ChangeTemplateFieldItemModel> getPaginatedFieldItems(Long changeTemplateId, int page,
                                                              int size);


    /**
     * Gets detail with roles.
     *
     * @param id       the id
     * @param userPage the user page
     * @param userSize the user size
     * @return the detail with roles
     */
    ChangeTemplateModel getDetailWithRoles(Long id, int userPage, int userSize);


    /**
     * Gets detail with roles.
     *
     * @param id the id
     * @return the detail with roles
     */
    ChangeTemplateModel getDetailWithRoles(Long id);
}
