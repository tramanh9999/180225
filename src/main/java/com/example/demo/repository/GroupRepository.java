package com.example.demo.repository;

import com.example.demo.entity.GroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    Page<GroupEntity> findByIsChangeRoleAndDeletedFalse(Boolean isChangeRole, Pageable pageable);

    List<GroupEntity> findAllByIdIn(List<Long> ids);
}
