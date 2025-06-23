package com.example.demo.repository;

import com.example.demo.entity.ChangeFlowNodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing ChangeFlowNodeEntity.
 */
@Repository
public interface ChangeFlowNodeRepository
        extends JpaRepository<ChangeFlowNodeEntity, Long>, ChangeFlowNodeRepositoryCustom {
    /*
     * Find all ChangeFlowNodeEntity by their IDs.
     *
     * @param ids the list of IDs to search for
     * @return the list of ChangeFlowNodeEntity with the given IDs
     */
    List<ChangeFlowNodeEntity> findByIdIn(List<Long> ids);
}