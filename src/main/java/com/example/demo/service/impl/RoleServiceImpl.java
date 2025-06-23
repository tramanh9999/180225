package com.example.demo.service.impl;

import com.example.demo.model.PagingRequestModel;
import com.example.demo.model.RoleModel;
import com.example.demo.service.RoleService;
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
