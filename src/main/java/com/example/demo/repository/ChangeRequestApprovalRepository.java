package com.example.demo.repository;

import com.example.demo.entity.ChangeRequestApprovalEntity;
import com.example.demo.model.ChangeRequestApprovalModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;


public interface ChangeRequestApprovalRepository
        extends JpaRepository<ChangeRequestApprovalEntity, Long> {

    // Find approvals by change request ID
    List<ChangeRequestApprovalEntity> findByChangeRequestId(Long changeRequestId);

    // Find approvals by role user ID
    List<ChangeRequestApprovalEntity> findByChangeRequestRoleUserId(Long changeRequestRoleUserId);

    // Find approvals by status
    List<ChangeRequestApprovalEntity> findByOverallStatus(String status);

    // Find approvals by change request ID and status
    List<ChangeRequestApprovalEntity> findByChangeRequestIdAndOverallStatus(Long changeRequestId,
                                                                            String status);

    List<ChangeRequestApprovalModel> findByChangeRequestRoleUserIdIn(
            Collection<Long> changeRequestRoleUserIds);
}
