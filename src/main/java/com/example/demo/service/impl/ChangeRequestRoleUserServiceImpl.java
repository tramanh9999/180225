package com.example.demo.service.impl;

import com.example.demo.entity.ChangeRequestRoleUserEntity;
import com.example.demo.mapper.ChangeRequestRoleUserMapper;
import com.example.demo.model.BusinessException;
import com.example.demo.model.ChangeRequestRoleUserModel;
import com.example.demo.model.ErrorCodeCommon;
import com.example.demo.repository.ChangeRequestRoleUserRepository;
import com.example.demo.service.ChangeRequestRoleUserService;
import com.example.demo.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing Change Request Role Users.
 * This service handles operations related to users assigned to specific roles
 * within a change request.
 */
@Service
@RequiredArgsConstructor
public class ChangeRequestRoleUserServiceImpl implements ChangeRequestRoleUserService {

    private final ChangeRequestRoleUserRepository changeRequestRoleUserRepository;
    private final ChangeRequestRoleUserMapper changeRequestRoleUserMapper;
    private final SysUserService sysUserService; // Inject SysUserService

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ChangeRequestRoleUserModel save(ChangeRequestRoleUserModel model) {
        ChangeRequestRoleUserEntity entity = changeRequestRoleUserMapper.toEntity(model);
        ChangeRequestRoleUserEntity savedEntity = changeRequestRoleUserRepository.save(entity);
        return changeRequestRoleUserMapper.toDto(savedEntity);
    }

    @Override
    public List<ChangeRequestRoleUserModel> findAllByChangeRequestId(Long changeRequestId) {
        List<ChangeRequestRoleUserEntity> changeRequestRoles =
                changeRequestRoleUserRepository.findAllByChangeRequestId(changeRequestId);
        return changeRequestRoles.stream().map(changeRequestRoleUserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveAll(List<ChangeRequestRoleUserModel> userModels) {
        if (userModels == null || userModels.isEmpty()) {
            return;
        }
        List<ChangeRequestRoleUserEntity> userEntities =
                userModels.stream().map(changeRequestRoleUserMapper::toEntity)
                        .collect(Collectors.toList());
        changeRequestRoleUserRepository.saveAll(userEntities);
    }


    @Transactional
    @Override
    public void deleteAllByChangeRequestId(Long changeRequestId) {
        changeRequestRoleUserRepository.deleteAllByChangeRequestId(changeRequestId);
    }

    @Override
    public void validateList(List<ChangeRequestRoleUserModel> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        List<String> invalidUsernames = new ArrayList<>();
        List<String> distinctUsernames =
                list.stream().map(ChangeRequestRoleUserModel::getUsername).distinct().toList();
        for (String username : distinctUsernames) {
            if (username != null && !username.trim().isEmpty()) {
                if (sysUserService.findByUsername(username).isEmpty()) {
                    invalidUsernames.add(username);
                }
            }
        }
        if (!invalidUsernames.isEmpty()) {
            String nonExistentUsernames = String.join(", ", invalidUsernames);
            throw new BusinessException(ErrorCodeCommon.USERNAME_NOT_FOUND, nonExistentUsernames);
        }
    }
}