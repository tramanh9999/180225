package vn.com.mbbank.kanban.mbamt.server.repository;

public interface ChangeTemplateRoleUserRepositoryCustom {
    /**
     * Count usernames by role id
     *
     * @param changeTemplateRoleId
     * @return number of usernames
     */
    long countUsernamesByRoleIdNative(Long changeTemplateRoleId);
}