package com.example.demo.repository;

import com.example.demo.entity.entity.ChangeRequestRoleUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for ChangeRequestRoleUserEntity.
 */
@Repository
public interface ChangeRequestRoleUserRepository
        extends JpaRepository<ChangeRequestRoleUserEntity, Long> {
    List<ChangeRequestRoleUserEntity> findAllByChangeRequestId(Long changeRequestId);


    /**
     * Delete all by change request id.
     *
     * @param changeRequestId the change request id
     */
    void deleteAllByChangeRequestId(Long changeRequestId);
}
