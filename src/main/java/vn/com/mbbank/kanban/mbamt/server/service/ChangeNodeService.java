package vn.com.mbbank.kanban.mbamt.server.service;

import vn.com.mbbank.kanban.mbamt.server.model.ChangeNodeModel;

import java.util.List;

public interface ChangeNodeService {
    //implement change node service methods here

    /**
     * Method to handle change node operations.
     * This could include creating, updating, deleting, or retrieving change nodes.
     */
    List<ChangeNodeModel> findAllByIds(List<Long> ids);
}
