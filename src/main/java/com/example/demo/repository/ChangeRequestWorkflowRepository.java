package com.example.demo.repository;

import com.example.demo.entity.ChangeRequestWorkflowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing ChangeRequestWorkflowEntity.
 */
@Repository
public interface ChangeRequestWorkflowRepository
        extends JpaRepository<ChangeRequestWorkflowEntity, Long> {
    /**
     * Find all ChangeRequestWorkflowEntity by their IDs.
     *
     * @param ids the list of IDs to search for
     * @return the list of ChangeRequestWorkflowEntity with the given IDs
     */
    List<ChangeRequestWorkflowEntity> findByIdIn(List<Long> ids);

}