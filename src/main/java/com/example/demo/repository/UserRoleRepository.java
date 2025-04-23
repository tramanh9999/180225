package com.example.demo.repository;

import com.example.demo.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    Collection<UserRoleEntity> findByUserId(Long userId);
}
