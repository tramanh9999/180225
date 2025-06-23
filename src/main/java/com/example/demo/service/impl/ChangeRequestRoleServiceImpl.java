package com.example.demo.service.impl;

import com.example.demo.entity.ChangeRequestRoleEntity;
import com.example.demo.mapper.ChangeRequestRoleMapper;
import com.example.demo.model.*;
import com.example.demo.repository.ChangeRequestRoleRepository;
import com.example.demo.service.ChangeFlowNodeService;
import com.example.demo.service.ChangeRequestRoleService;
import com.example.demo.service.ChangeRequestRoleUserService;
import com.example.demo.service.ChangeRequestWorkflowService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of ChangeRequestRoleService for managing Change Request Roles.
 */
@Service
@RequiredArgsConstructor
public class ChangeRequestRoleServiceImpl implements ChangeRequestRoleService {

    private final ChangeRequestRoleRepository repository;
    private final ChangeRequestRoleUserService changeRequestRoleUserService;
    private final ChangeRequestRoleMapper changeRequestRoleMapper;
    private final ChangeFlowNodeService changeFlowNodeService;
    private final ChangeRequestWorkflowService changeRequestWorkflowService;


    @Transactional
    @Override
    public void saveList(Long changeRequestId, List<ChangeRequestRoleModel> roles) {
        if (changeRequestId == null) {
            throw new BusinessException(ErrorCodeCommon.ID_NOT_FOUND, changeRequestId);
        }

        validateList(roles);

        changeRequestRoleUserService.deleteAllByChangeRequestId(changeRequestId);
        repository.deleteAllByChangeRequestId(changeRequestId);

        if (roles == null || roles.isEmpty()) {
            return;
        }

        List<ChangeRequestRoleUserModel> allUserModelsToSave = new ArrayList<>();

        for (ChangeRequestRoleModel roleModel : roles) {
            roleModel.setChangeRequestId(changeRequestId);
            roleModel.setId(null);

            ChangeRequestRoleEntity roleEntity = changeRequestRoleMapper.toEntity(roleModel);
            ChangeRequestRoleEntity savedRoleEntity = repository.save(roleEntity);

            if (roleModel.getUsers() != null && !roleModel.getUsers().isEmpty()) {
                roleModel.getUsers().forEach(userModel -> {
                    userModel.setChangeRequestRoleId(savedRoleEntity.getId());
                    userModel.setId(null);
                    allUserModelsToSave.add(userModel);
                });
            }
        }
        if (!allUserModelsToSave.isEmpty()) {
            changeRequestRoleUserService.saveAll(allUserModelsToSave);
        }
    }

    @Override
    public List<ChangeRequestRoleModel> findAllByChangeRequestId(Long changeRequestId) {
        if (changeRequestId == null) {
            throw new BusinessException(ErrorCodeCommon.ID_NOT_FOUND, changeRequestId);
        }
        if (!repository.existsByChangeRequestId(changeRequestId)) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_ID_NOT_FOUND,
                    changeRequestId);
        }

        List<ChangeRequestRoleEntity> roles = repository.findAllByChangeRequestId(changeRequestId);
        List<ChangeRequestRoleUserModel> users =
                changeRequestRoleUserService.findAllByChangeRequestId(changeRequestId);
        Map<Long, List<ChangeRequestRoleUserModel>> usersByChangeRequestRoleId = users.stream()
                .collect(Collectors.groupingBy(ChangeRequestRoleUserModel::getChangeRequestRoleId));
        return roles.stream().map(roleEntity -> {
            List<ChangeRequestRoleUserModel> associatedUsers =
                    usersByChangeRequestRoleId.getOrDefault(roleEntity.getId(),
                            Collections.emptyList());
            return changeRequestRoleMapper.toDto(roleEntity, associatedUsers);
        }).collect(Collectors.toList());
    }

    @Override
    public void validateList(List<ChangeRequestRoleModel> roles) {
        if (roles == null || roles.isEmpty()) {
            return;
        }

        Map<Long, ChangeFlowNodeModel> changeFlowNodeModelMap = changeFlowNodeService.findByIdIn(
                roles.stream().map(ChangeRequestRoleModel::getChangeFlowNodeId).toList());
        Map<Long, ChangeRequestWorkflowModel> changeRequestWorkflowModelMap =
                changeRequestWorkflowService.findByIdIn(
                        roles.stream().map(ChangeRequestRoleModel::getChangeRequestWorkflowId)
                                .toList());
        for (ChangeRequestRoleModel role : roles) {
            if (role.getChangeFlowNodeId() == null) {
                throw new BusinessException(ErrorCodeCommon.CHANGE_FLOW_NODE_ID_REQUIRED);
            }

            if (role.getChangeRequestWorkflowId() == null) {
                throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_WORKFLOW_ID_REQUIRED);
            }

            if (!changeFlowNodeModelMap.containsKey(role.getChangeFlowNodeId())) {
                throw new BusinessException(ErrorCodeCommon.CHANGE_FLOW_NODE_ID_NOT_FOUND,
                        role.getChangeFlowNodeId());
            }
            if (!changeRequestWorkflowModelMap.containsKey(role.getChangeRequestWorkflowId())) {
                throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_WORKFLOW_ID_NOT_FOUND,
                        role.getChangeRequestWorkflowId());
            }
        }

        //todo validate username in list group user
        changeRequestRoleUserService.validateList(
                roles.stream().flatMap(role -> role.getUsers().stream()).toList());
    }


    /**
     * Lấy ra tất cả các ChangeRequestRoleModel mà một username được chỉ định thuộc về,
     * trong một changeRequestId cụ thể.
     *
     * @param allChangeRequestRoles Danh sách tất cả các ChangeRequestRoleModel có sẵn.
     * @param changeRequestId       ID của Change Request cần lọc.
     * @param username              Tên người dùng cần tìm role.
     * @return Danh sách các ChangeRequestRoleModel mà username đó có role trong Change Request được chỉ định.
     */
    public List<ChangeRequestRoleModel> getRolesForUserInChangeRequest(
            List<ChangeRequestRoleModel> allChangeRequestRoles, Long changeRequestId,
            String username) {

        if (allChangeRequestRoles == null || username == null) {
            return new ArrayList<>();
        }

        return allChangeRequestRoles.stream()
                // Lọc theo changeRequestId trước để giảm số lượng đối tượng cần xử lý
                .filter(role -> Objects.equals(role.getChangeRequestId(), changeRequestId))
                // Kiểm tra xem username có trong danh sách 'users' hoặc 'cabUserGroups'
                // hoặc trong 'groupedCabUserGroups' của mỗi role hay không
                .filter(role -> {
                    // Kiểm tra danh sách 'users' (Approval roles)
                    boolean foundInUsers = role.getUsers() != null && role.getUsers().stream()
                            .anyMatch(user -> username.equals(user.getUsername()));

                    // Kiểm tra danh sách 'cabUserGroups' (CAB roles, nếu có)
                    boolean foundInCabUsers = role.getCabUserGroups() != null &&
                            role.getCabUserGroups().stream()
                                    .anyMatch(user -> username.equals(user.getUsername()));

                    // Kiểm tra danh sách 'groupedCabUserGroups' (CAB roles được nhóm)
                    // Đây là danh sách các list con của ChangeRequestRoleUserModel
                    boolean foundInGroupedCabUsers = role.getGroupedCabUserGroups() != null &&
                            role.getGroupedCabUserGroups().stream().flatMap(
                                            group -> group.getUsers() != null ? group.getUsers().stream() :
                                                    null) // Làm phẳng các danh sách con
                                    .filter(Objects::nonNull) // Lọc bỏ các phần tử null nếu có
                                    .anyMatch(user -> username.equals(user.getUsername()));

                    return foundInUsers || foundInCabUsers || foundInGroupedCabUsers;
                }).collect(Collectors.toList());
    }


}
