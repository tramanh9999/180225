package com.example.demo.service;

import com.example.demo.entity.ChangeTemplateFieldItemEntity;
import com.example.demo.entity.ChangeTemplateRoleEntity;
import com.example.demo.entity.ChangeTemplateRoleUserEntity;
import com.example.demo.mapper.ChangeTemplateFieldItemMapper;
import com.example.demo.mapper.ChangeTemplateMapper;
import com.example.demo.mapper.ChangeTemplateRoleMapper;
import com.example.demo.model.ChangeTemplateFieldItemDto;
import com.example.demo.model.ChangeTemplateModel;
import com.example.demo.model.ChangeTemplateRoleModel;
import com.example.demo.model.GroupModel;
import com.example.demo.model.LevelGroupModel;
import com.example.demo.repository.*;
import com.example.demo.service.dto.BusinessException;
import com.example.demo.service.dto.ErrorCodeCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        if (entity == null)
            return null;
        ChangeTemplateModel model = changeTemplateMapper.toDto(entity);
        // Lấy danh sách role order by level, roleOrder
        List<ChangeTemplateRoleEntity> roleEntities = changeTemplateRoleRepository
                .findByChangeTemplateIdOrderByLevelAscRoleOrderAsc(id);
        // Gom nhóm role theo level
        Map<Integer, List<ChangeTemplateRoleEntity>> groupByLevel = roleEntities.stream()
                .collect(Collectors.groupingBy(ChangeTemplateRoleEntity::getLevel));
        List<LevelGroupModel> levelGroups = groupByLevel.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    String levelId = String.valueOf(e.getKey());
                    String title = "Level " + e.getKey();
                    // Lấy danh sách groupId ở level này
                    List<Long> groupIds = e.getValue().stream().map(ChangeTemplateRoleEntity::getGroupId)
                            .collect(Collectors.toList());
                    List<GroupModel> groups = groupRepository.findAllByIdIn(groupIds).stream().map(g -> {
                        GroupModel gm = new GroupModel();
                        gm.setId(g.getId());
                        gm.setName(g.getName());
                        gm.setDescription(g.getDescription());
                        gm.setIsChangeRole(g.getIsChangeRole());
                        gm.setGroupType(g.getGroupType() != null ? g.getGroupType().getValue() : null);
                        return gm;
                    }).collect(Collectors.toList());
                    return LevelGroupModel.builder()
                            .id(levelId)
                            .title(title)
                            .changeRoles(groups)
                            .build();
                })
                .collect(Collectors.toList());
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
        validateRoles(model);
    }

    public void validateRoles(ChangeTemplateModel model) {
        if (model.getLevels() != null) {
            Set<Long> groupIds = model.getLevels().stream()
                    .flatMap(level -> level.getChangeRoles().stream())
                    .map(GroupModel::getId)
                    .collect(Collectors.toSet());
            // Validate groupId tồn tại
            List<Long> foundGroupIds = groupRepository.findAllByIdIn(groupIds.stream().toList()).stream()
                    .map(g -> g.getId()).toList();
            groupIds.stream().filter(id -> !foundGroupIds.contains(id)).findFirst()
                    .ifPresent(id -> {
                        throw new BusinessException(ErrorCodeCommon.GROUP_ID_NOT_FOUND);
                    });
        }
    }

    public ChangeTemplateModel saveInternal(ChangeTemplateModel changeTemplateModel) {
        validateChangeTemplateModel(changeTemplateModel);
        var entity = changeTemplateMapper.toEntity(changeTemplateModel);
        var savedEntity = changeTemplateRepository.save(entity);
        // Xử lý lưu levels
        saveRoles(savedEntity.getId(), changeTemplateModel);
        return changeTemplateMapper.toDto(savedEntity);
    }

    /**
     * Lưu roles và users cho template
     *
     * @param changeTemplateId    id của template
     * @param changeTemplateModel model template
     */
    public void saveRoles(Long changeTemplateId, ChangeTemplateModel changeTemplateModel) {
        if (changeTemplateModel.getLevels() != null) {
            changeTemplateRoleRepository.deleteByChangeTemplateId(changeTemplateId);
            // Lưu mới roles
            List<ChangeTemplateRoleEntity> roleEntities = changeTemplateModel.getLevels().stream()
                    .flatMap(level -> level.getChangeRoles().stream().map(group -> {
                        ChangeTemplateRoleEntity e = new ChangeTemplateRoleEntity();
                        e.setChangeTemplateId(changeTemplateId);
                        e.setGroupId(group.getId());
                        e.setLevel(Integer.valueOf(level.getId()));
                        e.setRoleOrder(0); // Nếu có logic order thì set lại
                        return e;
                    }))
                    .collect(Collectors.toList());
            changeTemplateRoleRepository.saveAll(roleEntities);
        }
    }

    /**
     * Gets paginated field item data for a Change Template.
     */
    @Override
    public Page<ChangeTemplateFieldItemDto> getPaginatedFieldItems(Long changeTemplateId, int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChangeTemplateFieldItemEntity> entityPage = changeTemplateFieldItemRepository.findByChangeTemplateId(
                changeTemplateId,
                pageable);

        // Map the entity page to a DTO page
        return entityPage.map(changeTemplateFieldItemMapper::toDto);
    }

    @Override
    public ChangeTemplateModel getDetailWithRoles(Long id, int userPage, int userSize) {
        return null;
    }

    @Override
    public ChangeTemplateModel getDetailWithRoles(Long id) {
        var entity = changeTemplateRepository.findById(id).orElse(null);
        if (entity == null)
            return null;
        ChangeTemplateModel model = changeTemplateMapper.toDto(entity);
        // Lấy danh sách role order by level, roleOrder
        List<ChangeTemplateRoleEntity> roleEntities = changeTemplateRoleRepository
                .findByChangeTemplateIdOrderByLevelAscRoleOrderAsc(id);
        // Gom nhóm role theo level
        Map<Integer, List<ChangeTemplateRoleEntity>> groupByLevel = roleEntities.stream()
                .collect(Collectors.groupingBy(ChangeTemplateRoleEntity::getLevel));
        List<LevelGroupModel> levelGroups = groupByLevel.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    String levelId = String.valueOf(e.getKey());
                    String title = "Level " + e.getKey();
                    List<Long> groupIds = e.getValue().stream().map(ChangeTemplateRoleEntity::getGroupId)
                            .collect(Collectors.toList());
                    List<GroupModel> groups = groupRepository.findAllByIdIn(groupIds).stream().map(g -> {
                        GroupModel gm = new GroupModel();
                        gm.setId(g.getId());
                        gm.setName(g.getName());
                        gm.setDescription(g.getDescription());
                        gm.setIsChangeRole(g.getIsChangeRole());
                        gm.setGroupType(g.getGroupType() != null ? g.getGroupType().getValue() : null);
                        return gm;
                    }).collect(Collectors.toList());
                    return LevelGroupModel.builder()
                            .id(levelId)
                            .title(title)
                            .changeRoles(groups)
                            .build();
                })
                .collect(Collectors.toList());
        model.setLevels(levelGroups);
        return model;
    }
}
