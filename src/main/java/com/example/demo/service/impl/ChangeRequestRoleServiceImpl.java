package com.example.demo.service.impl;

import com.example.demo.entity.entity.ChangeRequestRoleEntity;
import com.example.demo.mapper.ChangeRequestRoleMapper;
import com.example.demo.model.ChangeFlowNodeModel;
import com.example.demo.model.ChangeRequestRoleModel;
import com.example.demo.model.ChangeRequestRoleUserModel;
import com.example.demo.model.ChangeRequestWorkflowModel;
import com.example.demo.repository.ChangeRequestRoleRepository;
import com.example.demo.service.ChangeFlowNodeService;
import com.example.demo.service.ChangeRequestRoleService;
import com.example.demo.service.ChangeRequestRoleUserService;
import com.example.demo.service.ChangeRequestWorkflowService;
import com.example.demo.service.dto.BusinessException;
import com.example.demo.service.dto.ErrorCodeCommon;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
}
