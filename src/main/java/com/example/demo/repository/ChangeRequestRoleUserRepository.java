package com.example.demo.repository;

import com.example.demo.entity.ChangeRequestRoleUserEntity;
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

    //create custom query to find all by change request id and role id


    /**
     * Delete all by change request id.
     *
     * @param changeRequestId the change request id
     */
    void deleteAllByChangeRequestId(Long changeRequestId);

    /**
     * Find all by change request role id.
     *
     * @param changeRequestRoleId the change request role id
     * @return the list of ChangeRequestRoleUserEntity
     */
    List<ChangeRequestRoleUserEntity> findAllByChangeRequestRoleId(Long changeRequestRoleId);
}
