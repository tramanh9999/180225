package vn.com.mbbank.kanban.mbamt.server.repository;

import vn.com.mbbank.kanban.mbamt.server.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    Collection<UserRoleEntity> findByUserId(Long userId);
}
