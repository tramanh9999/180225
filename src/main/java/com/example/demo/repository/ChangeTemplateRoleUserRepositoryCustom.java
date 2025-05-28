package com.example.demo.repository;

public interface ChangeTemplateRoleUserRepositoryCustom {
    /**
     * Count usernames by role id
     *
     * @param changeTemplateRoleId
     * @return number of usernames
     */
    long countUsernamesByRoleIdNative(Long changeTemplateRoleId);
}