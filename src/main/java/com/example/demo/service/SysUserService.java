package com.example.demo.service;

import com.example.demo.entity.entity.SysUserEntity;

import java.util.Optional;

public interface SysUserService {
    /**
     * Finds a SysUserEntity by its username.
     *
     * @param username The username to search for.
     * @return An Optional containing the SysUserEntity if found, or empty otherwise.
     */
    Optional<SysUserEntity> findByUsername(String username);

    // Bạn có thể thêm các phương thức khác ở đây tùy theo nhu cầu:
    // SysUserEntity save(SysUserEntity user);
    // void deleteById(Long id);
    // List<SysUserEntity> findAll();
}