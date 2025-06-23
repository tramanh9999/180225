package com.example.demo.repository;

import com.example.demo.entity.SysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysUserRepository extends JpaRepository<SysUserEntity, Long> {
    Optional<SysUserEntity> findByUsername(String username);

    List<SysUserEntity> findAllByUsernameIn(List<String> usernames);
}
