package com.example.demo.service;

import com.example.demo.model.ChangeFlowNodeModel;
import com.example.demo.model.PagingRequestModel;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing Change Flow Nodes.
 */
public interface ChangeFlowNodeService {

    /**
     * Find all ChangeFlowNodeModel by their IDs.
     *
     * @param ids the list of IDs to search for
     * @return a map of ChangeFlowNodeModel with the ID as key and the model as value
     */
    Map<Long, ChangeFlowNodeModel> findByIdIn(List<Long> ids);

    /**
     * Find paging usernames by ChangeFlowNodeId
     *
     * @param id                 the id of ChangeFlowNodeModel
     * @param pagingRequestModel the paging request model
     * @return a page of usernames
     */
    Page<String> findPagingUsernameById(Long id, PagingRequestModel pagingRequestModel);
}
