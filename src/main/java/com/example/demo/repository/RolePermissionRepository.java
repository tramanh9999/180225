package com.example.demo.repository;


import com.example.demo.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    boolean existsByRoleIdInAndModuleActionIgnoreCaseAndPermissionActionIgnoreCaseAndTypeIgnoreCase(
            Set<Long> roleIds, String moduleAction, String permissionAction, String type);

    boolean existsByRoleIdInAndResourceIdAndModuleActionIgnoreCaseAndPermissionActionIgnoreCaseAndTypeIgnoreCase(
            Set<Long> roleIds, Long resourceId, String moduleAction, String permissionAction,
            String type);

    boolean existsByRoleIdInAndModuleActionIgnoreCaseAndSubModuleActionIgnoreCaseAndPermissionActionIgnoreCaseAndTypeIgnoreCase(
            Set<Long> roleIds, String moduleAction, String subModuleAction, String permissionAction,
            String type);

    boolean existsByRoleIdInAndResourceIdAndModuleActionIgnoreCaseAndSubModuleActionIgnoreCaseAndPermissionActionIgnoreCaseAndTypeIgnoreCase(
            Set<Long> roleIds, Long resourceId, String moduleAction, String subModuleAction,
            String permissionAction, String type);

    // Các phương thức khác có thể cần thiết
}
