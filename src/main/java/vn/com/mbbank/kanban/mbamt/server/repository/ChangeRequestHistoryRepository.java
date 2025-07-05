package vn.com.mbbank.kanban.mbamt.server.repository;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Make sure your ChangeRequestHistoryEntity is correctly imported
// import com.yourpackage.entity.ChangeRequestHistoryEntity;

@Repository // Marks this interface as a Spring Data JPA repository component
public interface ChangeRequestHistoryRepository
        extends JpaRepository<ChangeRequestHistoryEntity, Long> {

    /**
     * Finds all history records associated with a specific Change Request.
     * The results are ordered chronologically by their creation date in ascending order.
     *
     * @param changeRequestId The ID of the Change Request for which to retrieve history.
     * @return A List of ChangeRequestHistoryEntity objects. Returns an empty list if no history is found.
     */
    List<ChangeRequestHistoryEntity> findByChangeRequestIdOrderByCreatedDateAsc(
            Long changeRequestId);

    /**
     * Finds the most recent (latest) history record for a given Change Request.
     * This is useful for determining the current state or the state just before the last transition.
     *
     * @param changeRequestId The ID of the Change Request for which to find the latest history.
     * @return An Optional containing the latest ChangeRequestHistoryEntity, or an empty Optional if no history exists for the given ID.
     */
    Optional<ChangeRequestHistoryEntity> findTopByChangeRequestIdOrderByCreatedDateDesc(
            Long changeRequestId);

    // You can add more custom query methods here as needed, based on common lookup patterns.
    // For example:
    // List<ChangeRequestHistoryEntity> findByActionTaken(String actionTaken);
    // List<ChangeRequestHistoryEntity> findByChangeRequestIdAndNewChangeStatusId(Long changeRequestId, Long newChangeStatusId);
}