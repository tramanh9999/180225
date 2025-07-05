package vn.com.mbbank.kanban.mbamt.server.repository;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeTemplateRoleUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangeTemplateRoleUserRepository
        extends JpaRepository<ChangeTemplateRoleUserEntity, Long>,
        ChangeTemplateRoleUserRepositoryCustom {
    List<ChangeTemplateRoleUserEntity> findByChangeTemplateRoleId(Long changeTemplateRoleId);

    void deleteByChangeTemplateRoleId(Long changeTemplateRoleId);


    /**
     * Find usernames by role id
     *
     * @param changeTemplateRoleId changeTemplateRoleId
     * @return list of usernames
     */
    List<String> findUsernamesByChangeTemplateRoleId(Long changeTemplateRoleId);

}