package vn.com.mbbank.kanban.mbamt.server.service;

import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestWorkflowModel;
import org.springframework.transaction.annotation.Transactional;

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
    Map<Long, ChangeRequestWorkflowModel> getMapChangeWorkflowByIds(List<Long> ids);

    @Transactional(readOnly = true)
        // Read-only for performance optimization
    ChangeRequestWorkflowModel getWorkflowById(Long id);
}