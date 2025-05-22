package com.example.demo.service;

import com.example.demo.entity.ChangeTemplateFieldItemEntity;
import com.example.demo.entity.ChangeTemplateRoleEntity;
import com.example.demo.entity.ChangeTemplateRoleUserEntity;
import com.example.demo.mapper.ChangeTemplateFieldItemMapper;
import com.example.demo.mapper.ChangeTemplateMapper;
import com.example.demo.mapper.ChangeTemplateRoleMapper;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.dto.BusinessException;
import com.example.demo.service.dto.ErrorCodeCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the ChangeTemplateService.
 */
@Service
public class ChangeTemplateServiceImpl implements ChangeTemplateService {

    @Autowired
    private ChangeTemplateRepository changeTemplateRepository;

    @Autowired
    private ChangeTemplateMapper changeTemplateMapper;

    @Autowired
    private ChangeTemplateFieldItemRepository changeTemplateFieldItemRepository;

    @Autowired
    private ChangeTemplateFieldItemMapper changeTemplateFieldItemMapper;

    @Autowired
    private ChangeTemplateRoleRepository changeTemplateRoleRepository;

    @Autowired
    private ChangeTemplateRoleMapper changeTemplateRoleMapper;

    @Autowired
    private ChangeTemplateRoleUserRepository changeTemplateRoleUserRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Finds paginated Change Templates.
     */
    @Override
    public Page<ChangeTemplateModel> findPagings(Object paginationRequest) {
        // This implementation assumes paginationRequest can be converted to a Pageable
        // object.
        // A more robust implementation would handle different types of pagination
        // requests.
        // For now, returning an empty page or throwing an exception is a placeholder.
        // Example: Pageable pageable = ... convert paginationRequest to Pageable
        // return
        // changeTemplateRepository.findAll(pageable).map(changeTemplateMapper::toDto);
        return Page.empty(); // Placeholder implementation
    }

    /**
     * Deletes Change Templates by IDs.
     */
    @Override
    public void deleteByIds(List<Long> ids) {
        changeTemplateRepository.deleteAllById(ids);
    }

    /**
     * Finds a Change Template by ID.
     */
    @Override
    public ChangeTemplateModel findById(Long id) {
        var entity = changeTemplateRepository.findById(id).orElse(null);
        if (entity == null) return null;
        ChangeTemplateModel model = changeTemplateMapper.toDto(entity);
        // Lấy danh sách role order by level, roleOrder
        List<ChangeTemplateRoleEntity> roleEntities =
                changeTemplateRoleRepository.findByChangeTemplateIdOrderByLevelAscRoleOrderAsc(id);
        // Lấy user cho từng role (paging mặc định 0,10)
        int userPage = 0;
        int userSize = 10;
        List<ChangeTemplateRoleModel> roleModels = roleEntities.stream().map(role -> {
            ChangeTemplateRoleModel modelRole = changeTemplateRoleMapper.toModel(role);
            int offset = userPage * userSize;
            List<String> users =
                    changeTemplateRoleUserRepository.findUsernamesByRoleId(role.getId(),
                            PagingRequestModel.builder().page(userPage).size(userSize).build());
            modelRole.setUsers(users);
            return modelRole;
        }).toList();
        // Gom nhóm role cùng level
        Map<Integer, List<ChangeTemplateRoleModel>> groupByLevel = roleModels.stream()
                .collect(java.util.stream.Collectors.groupingBy(ChangeTemplateRoleModel::getLevel));
        List<LevelGroupModel> levelGroups =
                groupByLevel.entrySet().stream().sorted(Map.Entry.comparingByKey())
                        .map(e -> LevelGroupModel.builder().level(e.getKey()).roles(e.getValue())
                                .build()).toList();
        model.setLevels(levelGroups);
        return model;
    }

    /**
     * Saves a Change Template.
     */
    @Override
    public ChangeTemplateModel save(ChangeTemplateModel changeTemplateModel) {
        // Assuming save is for creating a new entity
        changeTemplateModel.setId(null); // Ensure ID is null for new entity
        return saveInternal(changeTemplateModel);
    }

    /**
     * Saves an existing Change Template by ID.
     */
    @Override
    public ChangeTemplateModel save(Long id, ChangeTemplateModel changeTemplateModel) {
        // Assuming save(id, model) is for updating an existing entity
        changeTemplateModel.setId(id); // Set the ID for update
        return saveInternal(changeTemplateModel);
    }

    private void validateChangeTemplateModel(ChangeTemplateModel model) {
        if (model.getName() == null || model.getName().trim().isEmpty()) {
            throw new BusinessException(ErrorCodeCommon.NAME_REQUIRED);
        }
        if (model.getLevels() != null) {
            Set<Long> groupIds = new HashSet<>();
            Set<String> usernames = new HashSet<>();
            for (LevelGroupModel group : model.getLevels()) {
                for (ChangeTemplateRoleModel role : group.getRoles()) {
                    if (role.getGroupId() == null) {
                        throw new BusinessException(ErrorCodeCommon.ROLE_GROUP_ID_REQUIRED);
                    }
                    groupIds.add(role.getGroupId());
                    if (role.getLevel() == null) {
                        throw new BusinessException(ErrorCodeCommon.ROLE_LEVEL_REQUIRED);
                    }
                    if (role.getUsers() != null) {
                        for (String user : role.getUsers()) {
                            if (user == null || user.trim().isEmpty()) {
                                throw new BusinessException(ErrorCodeCommon.USERNAME_REQUIRED);
                            }
                            usernames.add(user);
                        }
                    }
                }
            }
            // Validate groupId tồn tại
            List<Long> foundGroupIds =
                    groupRepository.findAllByIdIn(groupIds.stream().toList()).stream()
                            .map(g -> g.getId()).toList();
            for (Long id : groupIds) {
                if (!foundGroupIds.contains(id)) {
                    throw new BusinessException(ErrorCodeCommon.GROUP_ID_NOT_FOUND);
                }
            }
            // Validate username tồn tại
            List<String> foundUsernames =
                    userRepository.findAllByUsernameIn(usernames.stream().toList()).stream()
                            .map(u -> u.getUsername()).toList();
            for (String username : usernames) {
                if (!foundUsernames.contains(username)) {
                    throw new BusinessException(ErrorCodeCommon.USERNAME_NOT_FOUND);
                }
            }
        }
    }

    private ChangeTemplateModel saveInternal(ChangeTemplateModel changeTemplateModel) {
        validateChangeTemplateModel(changeTemplateModel);
        var entity = changeTemplateMapper.toEntity(changeTemplateModel);
        var savedEntity = changeTemplateRepository.save(entity);
        // Xử lý lưu levels
        if (changeTemplateModel.getLevels() != null) {
            changeTemplateRoleRepository.deleteByChangeTemplateId(savedEntity.getId());
            // Lưu mới
            List<ChangeTemplateRoleEntity> roleEntities = changeTemplateModel.getLevels().stream()
                    .flatMap(group -> group.getRoles().stream()).map(modelRole -> {
                        ChangeTemplateRoleEntity e = changeTemplateRoleMapper.toEntity(modelRole);
                        e.setChangeTemplateId(savedEntity.getId());
                        return e;
                    }).collect(Collectors.toList());
            List<ChangeTemplateRoleEntity> savedRoles =
                    changeTemplateRoleRepository.saveAll(roleEntities);
            // Lưu users cho từng role
            int roleIndex = 0;
            for (LevelGroupModel group : changeTemplateModel.getLevels()) {
                for (ChangeTemplateRoleModel modelRole : group.getRoles()) {
                    ChangeTemplateRoleEntity roleEntity = savedRoles.get(roleIndex++);
                    changeTemplateRoleUserRepository.deleteByChangeTemplateRoleId(
                            roleEntity.getId());
                    if (modelRole.getUsers() != null && !modelRole.getUsers().isEmpty()) {
                        List<ChangeTemplateRoleUserEntity> userEntities =
                                modelRole.getUsers().stream()
                                        .map(username -> ChangeTemplateRoleUserEntity.builder()
                                                .changeTemplateRoleId(roleEntity.getId())
                                                .username(username).build())
                                        .collect(Collectors.toList());
                        changeTemplateRoleUserRepository.saveAll(userEntities);
                    }
                }
            }
        }
        return changeTemplateMapper.toDto(savedEntity);
    }

    /**
     * Gets paginated field item data for a Change Template.
     */
    @Override
    public Page<ChangeTemplateFieldItemDto> getPaginatedFieldItems(Long changeTemplateId, int page,
                                                                   int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChangeTemplateFieldItemEntity> entityPage =
                changeTemplateFieldItemRepository.findByChangeTemplateId(changeTemplateId,
                        pageable);

        // Map the entity page to a DTO page
        return entityPage.map(changeTemplateFieldItemMapper::toDto);
    }

    @Override
    public ChangeTemplateModel getDetailWithRoles(Long id, int userPage, int userSize) {
        return null;
    }

    @Override
    public ChangeTemplateModel getDetailWithRoles(Long id, PagingRequestModel userPaging) {
        var entity = changeTemplateRepository.findById(id).orElse(null);
        if (entity == null) return null;
        ChangeTemplateModel model = changeTemplateMapper.toDto(entity);
        // Lấy danh sách role order by level, roleOrder
        List<ChangeTemplateRoleEntity> roleEntities =
                changeTemplateRoleRepository.findByChangeTemplateIdOrderByLevelAscRoleOrderAsc(id);
        // Lấy user cho từng role (paging)
        List<ChangeTemplateRoleModel> roleModels = roleEntities.stream().map(role -> {
            ChangeTemplateRoleModel modelRole = changeTemplateRoleMapper.toModel(role);
            // Paging user
            List<String> users =
                    changeTemplateRoleUserRepository.findUsernamesByRoleId(role.getId(),
                            userPaging);
            modelRole.setUsers(users);
            return modelRole;
        }).toList();
        // Gom nhóm role cùng level
        Map<Integer, List<ChangeTemplateRoleModel>> groupByLevel = roleModels.stream()
                .collect(java.util.stream.Collectors.groupingBy(ChangeTemplateRoleModel::getLevel));
        List<LevelGroupModel> levelGroups =
                groupByLevel.entrySet().stream().sorted(Map.Entry.comparingByKey())
                        .map(e -> LevelGroupModel.builder().level(e.getKey()).roles(e.getValue())
                                .build()).toList();
        model.setLevels(levelGroups);
        return model;
    }
}
