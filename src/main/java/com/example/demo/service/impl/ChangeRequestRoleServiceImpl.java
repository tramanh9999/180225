package com.example.demo.service.impl;

import com.example.demo.entity.ChangeRequestRoleEntity;
import com.example.demo.mapper.ChangeRequestRoleMapper;
import com.example.demo.model.*;
import com.example.demo.repository.ChangeRequestRoleRepository;
import com.example.demo.repository.ChangeRequestService;
import com.example.demo.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of ChangeRequestRoleService for managing Change Request Roles.
 */
@Service
@RequiredArgsConstructor
public class ChangeRequestRoleServiceImpl implements ChangeRequestRoleService {

    // Use @Autowired for field injection.
    // Fields should NOT be final when injected this way (as they are set after construction).
    @Autowired
    private ChangeRequestRoleRepository repository;

    @Autowired
    private ChangeRequestRoleUserService changeRequestRoleUserService;

    @Autowired
    private ChangeRequestRoleMapper changeRequestRoleMapper;

    @Autowired
    private ChangeFlowNodeService changeFlowNodeService;

    @Autowired
    private ChangeRequestWorkflowService changeRequestWorkflowService;

    // For the circular dependency, apply @Lazy and @Autowired.
    // This field also cannot be final.
    @Lazy // This is still necessary to break the cycle
    @Autowired
    private ChangeRequestService changeRequestService;
    @Autowired
    private ChangeRequestApprovalService changeRequestApprovalService;


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
        List<ChangeRequestRoleModel> collect = roles.stream().map(roleEntity -> {
            List<ChangeRequestRoleUserModel> associatedUsers =
                    usersByChangeRequestRoleId.getOrDefault(roleEntity.getId(),
                            Collections.emptyList());

            // Lấy danh sách các CAB user groups đã được nhóm

            return changeRequestRoleMapper.toDto(roleEntity, associatedUsers);
        }).collect(Collectors.toList());
        // Lấy danh sách các CAB user groups đã được nhóm
        collect = groupAndSortCabUserGroups2(collect);
        return collect;
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


    @Override
    public List<ChangeRequestRoleModel> findAllChangeFlowNodesByChangeTemplateIdOrRequestId(
            Long changeTemplateId, Long changeRequestId) throws BusinessException {

        var isNotSelectChangeTemplate = changeTemplateId == null || changeTemplateId == 0L;
        if (isNotSelectChangeTemplate) {
            return Collections.emptyList();
        }

        var defaultCreateChangeId = 0L;
        if (changeRequestId == null) {
            changeRequestId = defaultCreateChangeId;
        }

        var isCreateChange = Objects.equals(changeRequestId, defaultCreateChangeId);
        if (!isCreateChange) {
            ChangeRequestModel changeRequest = changeRequestService.findById(changeRequestId);
            if (!Objects.equals(changeRequest.getChangeTemplateId(), changeTemplateId)) {
                throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_TEMPLATE_HAS_CHANGE,
                        changeTemplateId);
            }
        }

        List<ChangeFlowNodeModel> flowNodeModelList =
                changeFlowNodeService.findChangeFlowNodesByTemplateId(changeTemplateId);
        Map<Long, ChangeFlowNodeModel> mapFlowNode = flowNodeModelList.stream()
                .collect(Collectors.toMap(ChangeFlowNodeModel::getId, Function.identity()));

        List<ChangeRequestRoleModel> requestRoleLst = new ArrayList<>();
        Map<Long, List<ChangeRequestRoleUserModel>> usersByChangeRequestRoleId = new HashMap<>();

        if (changeRequestId != defaultCreateChangeId) {
            requestRoleLst = changeRequestRoleMapper.mapToDtos(
                            repository.findAllByChangeRequestId(changeRequestId)).stream()
                    .filter(role -> Objects.nonNull(role.getChangeFlowNodeId()) &&
                            mapFlowNode.containsKey(role.getChangeFlowNodeId()))
                    .peek(role -> role.setChangeFlowNode(
                            mapFlowNode.get(role.getChangeFlowNodeId()))).toList();

            List<ChangeRequestRoleUserModel> users =
                    changeRequestRoleUserService.findAllByChangeRequestId(changeRequestId);
            usersByChangeRequestRoleId = users.stream().collect(
                    Collectors.groupingBy(ChangeRequestRoleUserModel::getChangeRequestRoleId));
        }

        Map<Long, ChangeRequestRoleModel> mapNodeWithRole = requestRoleLst.stream().collect(
                Collectors.toMap(ChangeRequestRoleModel::getChangeFlowNodeId, Function.identity()));

        AtomicInteger index = new AtomicInteger(flowNodeModelList.size());
        Map<Long, List<ChangeRequestRoleUserModel>> finalUsersByChangeRequestRoleId =
                usersByChangeRequestRoleId;
        Long finalChangeRequestId = changeRequestId;

        requestRoleLst = flowNodeModelList.stream().map(flowNode -> {
            if (mapNodeWithRole.containsKey(flowNode.getId())) {
                return mapNodeWithRole.get(flowNode.getId());
            }
            ChangeRequestRoleModel role =
                    ChangeRequestRoleModel.builder().id((long) index.getAndDecrement())
                            .changeFlowNodeId(flowNode.getId()).changeFlowNode(flowNode)
                            .changeRequestId(finalChangeRequestId).changeFlowNode(flowNode).build();

            finalUsersByChangeRequestRoleId.put(role.getId(),
                    List.of(ChangeRequestRoleUserModel.builder().username(Strings.EMPTY).id(0L)
                            .changeRequestRoleId(role.getId()).build()));
            return role;
        }).toList();


        requestRoleLst = groupAndSortCabUserGroups2(requestRoleLst);

        return requestRoleLst;
    }


    /**
     * Find ChangeRequestRoleModel by changeFlowNodeId and changeRequestId.
     *
     * @param changeRequestId  the ID of the change request
     * @param changeTemplateId the ID of the change template
     * @param changeFlowNodeId the ID of the change flow node
     * @return ChangeRequestRoleModel if found, otherwise throws BusinessException
     */
    @Override
    public ChangeRequestRoleModel getChangeRequestRoleByChangeFlowNodeId(Long changeRequestId,
                                                                         Long changeTemplateId,
                                                                         Long changeFlowNodeId) {


        List<ChangeRequestRoleModel> roleList =
                findAllChangeFlowNodesByChangeTemplateIdOrRequestId(changeTemplateId,
                        changeRequestId);

        ChangeRequestRoleModel changeRole = roleList.stream()
                .filter(role -> changeFlowNodeId.equals(role.getChangeFlowNode().getId()))
                .findFirst().orElseThrow(
                        () -> new BusinessException(ErrorCodeCommon.CHANGE_FLOW_NODE_ID_NOT_FOUND));
        List<ChangeRequestRoleUserModel> roleUserList =
                changeRequestRoleUserService.findAllByChangeRequestRoleId(changeRole.getId());

        List<Long> roleUserIds =
                roleUserList.stream().map(ChangeRequestRoleUserModel::getId).toList();
        if (roleUserIds.isEmpty()) {
            return changeRole;
        }

        List<ChangeRequestApprovalModel> approvals =
                changeRequestApprovalService.findByChangeRequestRoleUserIdIn(roleUserIds);

        var approvalMap = approvals.stream().collect(
                Collectors.toMap(ChangeRequestApprovalModel::getChangeRequestRoleUserId,
                        approval -> approval, (existing, replacement) -> existing));

        // set value of ChangeRequestRoleUserModel.approvalModel to provided ChangeRequestApprovalModel
        roleUserList.forEach(roleUser -> {
            ChangeRequestApprovalModel approval = approvalMap.get(roleUser.getId());
            if (approval != null) {
                roleUser.setApprovalModel(approval);
            }
        });
        return changeRole;
    }

    @Override
    public List<ChangeRequestRoleModel> groupAndSortCabUserGroups2(
            List<ChangeRequestRoleModel> items) {
        if (items == null || items.isEmpty()) {
            return items;
        }

        for (ChangeRequestRoleModel item : items) {
            List<ChangeRequestRoleUserModel> cabUsers = item.getCabUserGroups();
            if (cabUsers == null || cabUsers.isEmpty()) {
                item.setWorkflows(Collections.emptyList());
                continue;
            }

            Map<Long, List<ChangeRequestRoleUserModel>> groupedByWorkflow =
                    cabUsers.stream().collect(Collectors.groupingBy(user -> {
                        if (user.getChangeRequestWorkflowId() != null) {
                            return user.getChangeRequestWorkflowId();
                        }
                        return 0L;
                    }));

            Set<Long> setWorkflowIds = groupedByWorkflow.keySet();
            Map<Long, ChangeRequestWorkflowModel> mapWorkflows =
                    changeRequestWorkflowService.findByIdIn(setWorkflowIds.stream().toList());

            List<ChangeRequestRoleUserWorkflowListModel> workflowUsersList = new ArrayList<>();
            for (Map.Entry<Long, List<ChangeRequestRoleUserModel>> workflowEntry : groupedByWorkflow.entrySet()) {
                List<ChangeRequestRoleUserModel> usersInCurrentWorkflow = workflowEntry.getValue();
                List<ChangeRequestRoleUserModel> usersWithNonNullCabGroup =
                        usersInCurrentWorkflow.stream().filter(user -> user.getCabGroup() != null)
                                .toList();
                Map<Integer, List<ChangeRequestRoleUserModel>> groupedByCabGroup = new TreeMap<>();
                if (!usersWithNonNullCabGroup.isEmpty()) {
                    groupedByCabGroup = usersWithNonNullCabGroup.stream().collect(
                            Collectors.groupingBy(ChangeRequestRoleUserModel::getCabGroup,
                                    () -> new TreeMap<>(Comparator.naturalOrder()),
                                    Collectors.toList()));
                }
                List<List<ChangeRequestRoleUserModel>> sorted2DList = new ArrayList<>();
                for (Map.Entry<Integer, List<ChangeRequestRoleUserModel>> entry : groupedByCabGroup.entrySet()) {
                    List<ChangeRequestRoleUserModel> group = entry.getValue();
                    List<ChangeRequestRoleUserModel> sortedGroup = group.stream()
                            .sorted(Comparator.comparing(
                                    ChangeRequestRoleUserModel::getCabGroupOrder,
                                    Comparator.nullsLast(Comparator.naturalOrder())))
                            .collect(Collectors.toList());
                    sorted2DList.add(sortedGroup);
                }
                List<ChangeRequestRoleUserListModel> cabUserListModels = sorted2DList.stream()
                        .map(oneGroup -> ChangeRequestRoleUserListModel.builder().users(oneGroup)
                                .build()).collect(Collectors.toList());
                workflowUsersList.add(ChangeRequestRoleUserWorkflowListModel.builder()
                        .changeWorkflowId(workflowEntry.getKey()).changeWorkflowName(
                                mapWorkflows.getOrDefault(workflowEntry.getKey(),
                                        ChangeRequestWorkflowModel.builder()
                                                .name("Not found workflow" + workflowEntry.getKey())
                                                .build()).getName()).groups(cabUserListModels)
                        .build());
            }
            item.setWorkflows(workflowUsersList);
        }

        return items;
    }
}
