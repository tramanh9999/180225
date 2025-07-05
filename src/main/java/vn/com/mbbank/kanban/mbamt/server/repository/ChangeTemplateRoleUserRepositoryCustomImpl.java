package vn.com.mbbank.kanban.mbamt.server.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ChangeTemplateRoleUserRepositoryCustomImpl
        implements ChangeTemplateRoleUserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long countUsernamesByRoleIdNative(Long changeTemplateRoleId) {
        String sql =
                "SELECT COUNT(*) FROM CHANGE_TEMPLATE_ROLE_USER WHERE change_template_role_id = :roleId";
        Object result =
                entityManager.createNativeQuery(sql).setParameter("roleId", changeTemplateRoleId)
                        .getSingleResult();
        if (result instanceof Number) {
            return ((Number) result).longValue();
        }
        return 0;
    }
}