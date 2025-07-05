package vn.com.mbbank.kanban.mbamt.server.repository;

import vn.com.mbbank.kanban.mbamt.server.entity.RoleGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RoleGroupRepository extends JpaRepository<RoleGroupEntity, Long> {
    Collection<RoleGroupEntity> findByGroupIdIn(List<Long> groupIds);
}
