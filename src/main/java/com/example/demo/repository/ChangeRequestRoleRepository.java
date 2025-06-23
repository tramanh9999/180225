package com.example.demo.repository;

import com.example.demo.entity.ChangeRequestRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository interface for managing ChangeRequestRoleEntity.
 * Extends both JpaRepository for standard operations and
 * ChangeRequestRoleCustomRepository for custom native SQL operations.
 */
@Repository
public interface ChangeRequestRoleRepository extends JpaRepository<ChangeRequestRoleEntity, Long> {
    /**
     * Check if any roles exist for the given change request ID.
     *
     * @param changeRequestId The change request ID to check
     * @return true if roles exist for the given change request ID, false otherwise
     */
    boolean existsByChangeRequestId(Long changeRequestId);

    /**
     * Find all roles by change request ID.
     *
     * @param changeRequestId The change request ID to search for
     * @return A list of ChangeRequestRoleEntity objects associated with the given
     * change request ID
     */
    List<ChangeRequestRoleEntity> findAllByChangeRequestId(Long changeRequestId);

    /**
     * Delete all roles by change request ID.
     *
     * @param changeRequestId The change request ID to delete roles for
     */
    void deleteAllByChangeRequestId(Long changeRequestId);
}