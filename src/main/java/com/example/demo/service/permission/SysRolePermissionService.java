package com.example.demo.service.permission;

import org.springframework.security.core.Authentication;

public interface SysRolePermissionService {


    boolean hasPermission(Authentication authentication, Long resourceId, String moduleAction,
                          String permissionAction, String type);

    boolean hasPermission(Authentication authentication, String moduleAction,
                          String permissionAction, String type);
}
