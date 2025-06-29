package com.example.demo.repository;

import com.example.demo.entity.ChangeRequestApprovalResultEntity;
import com.example.demo.enums.ApprovalResultStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing ChangeRequestApprovalResultEntity.
 * This interface extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface ChangeRequestApprovalResultRepository
        extends JpaRepository<ChangeRequestApprovalResultEntity, Long> {

    /**
     * Finds all approval user results associated with a specific change request approval ID.
     *
     * @param changeRequestApprovalId The ID of the parent ChangeRequestApprovalEntity.
     * @return A list of ChangeApprovalUserResultEntity.
     */
    List<ChangeRequestApprovalResultEntity> findByChangeRequestApprovalId(
            Long changeRequestApprovalId);

    /**
     * Finds an approval user result by the approval ID and the user who approved it.
     * This might be useful to check if a specific user has already responded.
     *
     * @param changeRequestApprovalId The ID of the parent ChangeRequestApprovalEntity.
     * @param approvedUser            The username or ID of the user.
     * @return An Optional containing the found entity, or empty if not found.
     */
    Optional<ChangeRequestApprovalResultEntity> findByChangeRequestApprovalIdAndApprovedUser(
            Long changeRequestApprovalId, String approvedUser);

    /**
     * Counts the number of approval results for a given approval ID by a specific status.
     *
     * @param changeRequestApprovalId The ID of the parent ChangeRequestApprovalEntity.
     * @param status                  The status to count (e.g., APPROVED, REJECTED).
     * @return The count of results matching the criteria.
     */
    long countByChangeRequestApprovalIdAndStatus(Long changeRequestApprovalId,
                                                 ApprovalResultStatus status);
}