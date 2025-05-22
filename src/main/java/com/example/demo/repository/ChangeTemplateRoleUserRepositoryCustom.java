package com.example.demo.repository;

import java.util.List;

import com.example.demo.model.PagingRequestModel;

public interface ChangeTemplateRoleUserRepositoryCustom {
    /**
     * Find usernames by role id
     * 
     * @param changeTemplateRoleId changeTemplateRoleId
     * @param pagingRequest        pagingRequest
     * @return list of usernames
     */
    List<String> findUsernamesByRoleId(Long changeTemplateRoleId, PagingRequestModel pagingRequest);

    /**
     * Count usernames by role id
     * 
     * @param changeTemplateRoleId
     * @return number of usernames
     */
    long countUsernamesByRoleIdNative(Long changeTemplateRoleId);
}