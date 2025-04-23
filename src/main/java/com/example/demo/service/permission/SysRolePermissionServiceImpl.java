package com.example.demo.service.permission;

import com.example.demo.entity.User;
import com.example.demo.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("aclSvc")
@Transactional(readOnly = true)
public class SysRolePermissionServiceImpl implements SysRolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final GroupUserRepository groupUserRepository;
    private final RoleGroupRepository roleGroupRepository;

    public SysRolePermissionServiceImpl(RolePermissionRepository rolePermissionRepository,
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            GroupUserRepository groupUserRepository,
            RoleGroupRepository roleGroupRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.groupUserRepository = groupUserRepository;
        this.roleGroupRepository = roleGroupRepository;
    }

    @Override
    public boolean hasPermission(Authentication authentication, String moduleAction,
            String permissionAction, String type) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return false;
        }

        Set<Long> roleIds = getUserRoleIds(user.getId());

        if (roleIds.isEmpty()) {
            return false;
        }

        return rolePermissionRepository
                .existsByRoleIdInAndModuleActionIgnoreCaseAndPermissionActionIgnoreCaseAndTypeIgnoreCase(
                        roleIds, moduleAction, permissionAction, type);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Long resourceId,
            String moduleAction, String permissionAction, String type) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return false;
        }

        Set<Long> roleIds = getUserRoleIds(user.getId());

        if (roleIds.isEmpty()) {
            return false;
        }

        return rolePermissionRepository
                .existsByRoleIdInAndResourceIdAndModuleActionIgnoreCaseAndPermissionActionIgnoreCaseAndTypeIgnoreCase(
                        roleIds, resourceId, moduleAction, permissionAction, type);
    }

    // Helper method to get all role IDs for a user (direct and via groups)
    private Set<Long> getUserRoleIds(Long userId) {
        Set<Long> roleIds = new HashSet<>();

        // Get direct roles
        List<Long> directRoleIds = userRoleRepository.findByUserId(userId).stream()
                .map(ur -> ur.getRoleId())
                .collect(Collectors.toList());
        roleIds.addAll(directRoleIds);

        // Get roles via groups
        List<Long> groupIds = groupUserRepository.findByUserId(userId).stream()
                .map(gu -> gu.getGroupId())
                .collect(Collectors.toList());

        List<Long> roleIdsViaGroups = roleGroupRepository.findByGroupIdIn(groupIds).stream()
                .map(rg -> rg.getRoleId())
                .collect(Collectors.toList());
        roleIds.addAll(roleIdsViaGroups);

        return roleIds;
    }
}
