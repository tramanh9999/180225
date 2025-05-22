package com.example.demo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import com.example.demo.model.PagingRequestModel;

import java.util.List;

@Repository
public class ChangeTemplateRoleUserRepositoryCustomImpl implements ChangeTemplateRoleUserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<String> findUsernamesByRoleId(Long changeTemplateRoleId, PagingRequestModel pagingRequest) {
        int offset = pagingRequest.getPage() * pagingRequest.getSize();
        int limit = pagingRequest.getSize();
        String sql = "SELECT username FROM CHANGE_TEMPLATE_ROLE_USER WHERE change_template_role_id = :roleId ORDER BY id OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY";

        return entityManager.createNativeQuery(sql)
                .setParameter("roleId", changeTemplateRoleId)
                .setParameter("offset", offset)
                .setParameter("limit", limit)
                .getResultList();
    }

    @Override
    public long countUsernamesByRoleIdNative(Long changeTemplateRoleId) {
        String sql = "SELECT COUNT(*) FROM CHANGE_TEMPLATE_ROLE_USER WHERE change_template_role_id = :roleId";
        Object result = entityManager.createNativeQuery(sql)
                .setParameter("roleId", changeTemplateRoleId)
                .getSingleResult();
        if (result instanceof Number) {
            return ((Number) result).longValue();
        }
        return 0;
    }
}