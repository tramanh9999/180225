package vn.com.mbbank.kanban.mbamt.server.service.impl;

import vn.com.mbbank.kanban.mbamt.server.model.PagingRequestModel;
import vn.com.mbbank.kanban.mbamt.server.model.RoleModel;
import vn.com.mbbank.kanban.mbamt.server.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {


    @Override
    public Page<RoleModel> findRoles(PagingRequestModel request) {
        return null;
    }

    @Override
    public Page<RoleModel> findUsersInRole(Long roleId, PagingRequestModel request) {
        return null;
    }
}
