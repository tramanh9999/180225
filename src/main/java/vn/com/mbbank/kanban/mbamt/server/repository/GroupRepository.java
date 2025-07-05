package vn.com.mbbank.kanban.mbamt.server.repository;

import vn.com.mbbank.kanban.mbamt.server.entity.SysGroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<SysGroupEntity, Long> {
    Page<SysGroupEntity> findByIsChangeRoleAndDeletedFalse(Boolean isChangeRole, Pageable pageable);

    List<SysGroupEntity> findAllByIdIn(List<Long> ids);
}
