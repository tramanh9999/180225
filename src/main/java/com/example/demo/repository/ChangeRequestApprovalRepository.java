package com.example.demo.repository;

import com.example.demo.entity.ChangeRequestApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * The interface Change request approval repository.
 */
public interface ChangeRequestApprovalRepository
        extends JpaRepository<ChangeRequestApprovalEntity, Long> {

    /**
     * Find by change request id list.
     *
     * @param changeRequestId the change request id
     * @return the list
     */
// Find approvals by change request ID
    List<ChangeRequestApprovalEntity> findByChangeRequestId(Long changeRequestId);

    /**
     * Find by change request role user id list.
     *
     * @param changeRequestRoleUserId the change request role user id
     * @return the list
     */
// Find approvals by role user ID
    List<ChangeRequestApprovalEntity> findByChangeRequestRoleUserId(Long changeRequestRoleUserId);

    
    /**
     * Find by change request role user id in list.
     *
     * @param changeRequestRoleUserIds the change request role user ids
     * @return the list
     */
    List<ChangeRequestApprovalEntity> findByChangeRequestRoleUserIdIn(
            List<Long> changeRequestRoleUserIds);
}
