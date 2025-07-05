package vn.com.mbbank.kanban.mbamt.server.repository;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestWorkflowDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing ChangeRequestWorkflowDetailEntity entities.
 */
@Repository
public interface ChangeRequestWorkflowDetailRepository
        extends JpaRepository<ChangeRequestWorkflowDetailEntity, Long> {

    /**
     * Finds all workflow details associated with a specific change request workflow, ordered by stepOrder.
     * Note: 'stepOrder' is not in ChangeRequestWorkflowDetailEntity.
     * If you need ordering, you'll need to add a 'stepOrder' column to ChangeRequestWorkflowDetailEntity.
     * For now, I'll remove 'OrderByStepOrder' or use another field if available.
     * Let's assume you'll add 'stepOrder' to the Entity if required for ordering.
     */
    List<ChangeRequestWorkflowDetailEntity> findByChangeRequestWorkflowId(
            Long changeRequestWorkflowId);

    /**
     * Finds a specific workflow detail by its workflow ID and Change Node ID.
     *
     * @param changeRequestWorkflowId The ID of the parent workflow.
     * @param changeNodeId            The ID of the change node.
     * @return An Optional containing the workflow detail, or empty if not found.
     */
    Optional<ChangeRequestWorkflowDetailEntity> findByChangeRequestWorkflowIdAndChangeNodeId(
            Long changeRequestWorkflowId, Long changeNodeId);

    List<ChangeRequestWorkflowDetailEntity> findByIdIn(Collection<Long> ids);
}