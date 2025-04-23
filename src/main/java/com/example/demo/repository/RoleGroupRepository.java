package com.example.demo.repository;

import com.example.demo.entity.RoleGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RoleGroupRepository extends JpaRepository<RoleGroupEntity, Long> {
    Collection<RoleGroupEntity> findByGroupIdIn(List<Long> groupIds);
}
