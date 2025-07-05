package vn.com.mbbank.kanban.mbamt.server.service;

import vn.com.mbbank.kanban.mbamt.server.model.PagingRequestModel;
import vn.com.mbbank.kanban.mbamt.server.model.RoleModel;
import org.springframework.data.domain.Page;

public interface RoleService {
    Page<RoleModel> findRoles(PagingRequestModel request);

    Page<RoleModel> findUsersInRole(Long roleId, PagingRequestModel request);
}
