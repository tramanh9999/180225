package com.example.demo.service;

import com.example.demo.model.PagingRequestModel;
import com.example.demo.model.RoleModel;
import org.springframework.data.domain.Page;

public interface RoleService {
    Page<RoleModel> findRoles(PagingRequestModel request);

    Page<RoleModel> findUsersInRole(Long roleId, PagingRequestModel request);
}
