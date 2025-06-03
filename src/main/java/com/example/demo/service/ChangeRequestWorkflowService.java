package com.example.demo.service;

import com.example.demo.model.ChangeRequestWorkflowModel;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing Change Request Workflows.
 */
public interface ChangeRequestWorkflowService {
    
    /**
     * Find all ChangeRequestWorkflowModel by their IDs.
     *
     * @param ids the list of IDs to search for
     * @return a map of ChangeRequestWorkflowModel with the ID as key and the model as value
     */
    Map<Long, ChangeRequestWorkflowModel> findByIdIn(List<Long> ids);
}