package com.example.demo.repository;

import com.example.demo.entity.entity.ChangeTemplateRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangeTemplateRoleRepository
        extends JpaRepository<ChangeTemplateRoleEntity, Long> {
    List<ChangeTemplateRoleEntity> findByChangeTemplateId(Long changeTemplateId);

    void deleteByChangeTemplateId(Long changeTemplateId);

    List<ChangeTemplateRoleEntity> findByChangeTemplateIdOrderByLevelAscRoleOrderAsc(
            Long changeTemplateId);
}