package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface GroupUserRepository extends JpaRepository<GroupUserEntity, Long> {
    Collection<GroupUserEntity> findByUserId(Long userId);
}
