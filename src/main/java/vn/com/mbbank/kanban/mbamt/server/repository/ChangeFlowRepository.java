package vn.com.mbbank.kanban.mbamt.server.repository;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeFlowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing ChangeFlowNodeEntity.
 */
@Repository
public interface ChangeFlowRepository extends JpaRepository<ChangeFlowEntity, Long> {
    /*
     * Find all ChangeFlowNodeEntity by their IDs.
     *
     * @param ids the list of IDs to search for
     * @return the list of ChangeFlowNodeEntity with the given IDs
     */
    List<ChangeFlowEntity> findByIdIn(List<Long> ids);
}