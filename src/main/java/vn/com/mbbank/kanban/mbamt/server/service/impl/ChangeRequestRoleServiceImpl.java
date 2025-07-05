package vn.com.mbbank.kanban.mbamt.server.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestRoleEntity;
import vn.com.mbbank.kanban.mbamt.server.enums.ChangeFlowNodeTypeEnum;
import vn.com.mbbank.kanban.mbamt.server.mapper.ChangeRequestRoleMapper;
import vn.com.mbbank.kanban.mbamt.server.model.*;
import vn.com.mbbank.kanban.mbamt.server.repository.ChangeRequestRoleRepository;
import vn.com.mbbank.kanban.mbamt.server.repository.ChangeRequestService;
import vn.com.mbbank.kanban.mbamt.server.service.*;
import vn.com.mbbank.kanban.mbamt.server.utils.KanbanCommonUtil;

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
    @Autowired
    private ChangeRequestWorkflowDetailService changeRequestWorkflowDetailService;


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

//    @Override
//    public List<ChangeRequestRoleModel> findAllByChangeRequestId(Long changeRequestId) {
//        if (changeRequestId == null) {
//            throw new BusinessException(ErrorCodeCommon.ID_NOT_FOUND, changeRequestId);
//        }
//        if (!repository.existsByChangeRequestId(changeRequestId)) {
//            throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_ID_NOT_FOUND,
//                    changeRequestId);
//        }
//
//        List<ChangeRequestRoleEntity> roles = repository.findAllByChangeRequestId(changeRequestId);
//        List<ChangeRequestRoleUserModel> users =
//                changeRequestRoleUserService.findAllByChangeRequestId(changeRequestId);
//
//
//        ///  map users to get changeRequestWorkflowDetailId
//
//
//        Map<Long, List<ChangeRequestRoleUserModel>> usersByChangeRequestRoleId = users.stream()
//                .collect(Collectors.groupingBy(ChangeRequestRoleUserModel::getChangeRequestRoleId));
//        List<ChangeRequestRoleModel> collect = roles.stream().map(roleEntity -> {
//            List<ChangeRequestRoleUserModel> associatedUsers =
//                    usersByChangeRequestRoleId.getOrDefault(roleEntity.getId(),
//                            Collections.emptyList());
//
//            // Lấy danh sách các CAB user groups đã được nhóm
//
//            return changeRequestRoleMapper.toDto(roleEntity, associatedUsers);
//        }).collect(Collectors.toList());
//        // Lấy danh sách các CAB user groups đã được nhóm
//        collect = getWorkflowAndGroup(collect);
//        return collect;
//    }

    @Override
    public void validateList(List<ChangeRequestRoleModel> roles) {
        if (roles == null || roles.isEmpty()) {
            return;
        }

        Map<Long, ChangeFlowNodeModel> changeFlowNodeModelMap = changeFlowNodeService.findByIdIn(
                roles.stream().map(ChangeRequestRoleModel::getChangeFlowNodeId).toList());
        Map<Long, ChangeRequestWorkflowModel> changeRequestWorkflowModelMap =
                changeRequestWorkflowService.getMapChangeWorkflowByIds(
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
                changeFlowNodeService.findAllChangeFlowNodesByChangeTemplateId(changeTemplateId);
        Map<Long, ChangeFlowNodeModel> mapFlowNode = flowNodeModelList.stream()
                .collect(Collectors.toMap(ChangeFlowNodeModel::getId, Function.identity()));

        List<ChangeRequestRoleModel> requestRoleLst = new ArrayList<>();
        Map<Long, List<ChangeRequestRoleUserModel>> mapRoleUserListWithRoleId = new HashMap<>();

        if (changeRequestId != defaultCreateChangeId) {
            requestRoleLst = changeRequestRoleMapper.mapToDtos(
                            repository.findAllByChangeRequestId(changeRequestId)).stream()
                    .filter(role -> Objects.nonNull(role.getChangeFlowNodeId()) &&
                            mapFlowNode.containsKey(role.getChangeFlowNodeId()))
                    .peek(role -> role.setChangeFlowNode(
                            mapFlowNode.get(role.getChangeFlowNodeId()))).toList();

            List<ChangeRequestRoleUserModel> allRoleUserInChangeRequest =
                    changeRequestRoleUserService.findAllByChangeRequestId(changeRequestId);

            //find all workflowId by workflowNodeDetailId in change request role user
            if (allRoleUserInChangeRequest == null || allRoleUserInChangeRequest.isEmpty()) {
                return requestRoleLst;
            }


            mapRoleUserListWithRoleId = allRoleUserInChangeRequest.stream().collect(
                    Collectors.groupingBy(ChangeRequestRoleUserModel::getChangeRequestRoleId));
        }

        Map<Long, ChangeRequestRoleModel> mapChangeRequestRoleWithNode = requestRoleLst.stream()
                .collect(Collectors.toMap(ChangeRequestRoleModel::getChangeFlowNodeId,
                        Function.identity()));

        AtomicInteger index = new AtomicInteger(flowNodeModelList.size());
        Map<Long, List<ChangeRequestRoleUserModel>> finalMapRoleUserListWithRoleId =
                mapRoleUserListWithRoleId;
        Long finalChangeRequestId = changeRequestId;

        requestRoleLst = flowNodeModelList.stream().map(flowNode -> {
            var role = mapChangeRequestRoleWithNode.getOrDefault(flowNode.getId(),
                    ChangeRequestRoleModel.builder().id((long) index.getAndDecrement())
                            .changeFlowNodeId(flowNode.getId()).changeFlowNode(flowNode)
                            .changeRequestId(finalChangeRequestId).changeFlowNode(flowNode)
                            .build());
            var roleUsers = finalMapRoleUserListWithRoleId.getOrDefault(role.getId(),
                    List.of(ChangeRequestRoleUserModel.builder().username(Strings.EMPTY).id(0L)
                            .changeRequestRoleId(role.getId()).build()));
            role.setUsers(roleUsers);
            return role;
        }).toList();


        requestRoleLst = getWorkflowAndGroup(requestRoleLst);

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

        if (roleList.isEmpty()) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_ROLE_NOT_CONFIGURED,
                    changeTemplateId);
        }
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
    public List<ChangeRequestRoleModel> getWorkflowAndGroup(List<ChangeRequestRoleModel> items) {
        if (items == null || items.isEmpty()) {
            return items;
        }

        //get all role user in list role;
        List<ChangeRequestRoleUserModel> allRoleUsers =
                items.stream().flatMap(item -> item.getUsers().stream()).toList();


        // create list of change request workflow detail ids
        List<Long> changeRequestWorkflowDetailIds = allRoleUsers.stream()
                .map(ChangeRequestRoleUserModel::getChangeRequestWorkflowDetailId)
                .filter(Objects::nonNull).distinct().toList();
        List<ChangeRequestWorkflowDetailModel> workflowDetailList =
                changeRequestWorkflowDetailService.findWorkflowDetailByIds(
                        changeRequestWorkflowDetailIds);
        var workflowDetailModelMap = workflowDetailList.stream().collect(
                Collectors.toMap(ChangeRequestWorkflowDetailModel::getId, Function.identity()));
        //create map of change request workflow id by model in workflowDetailList
        Map<Long, ChangeRequestWorkflowModel> mapWorkflows =
                changeRequestWorkflowService.getMapChangeWorkflowByIds(workflowDetailList.stream()
                        .map(ChangeRequestWorkflowDetailModel::getChangeRequestWorkflowId)
                        .distinct().toList());


        //set   private Long changeRequestWorkflowId;
        //    private Long changeRequestWorkflowName;
        //    private Long changeNodeId;
        //    private String changeNodeName; to role user model in allRoleUsers

        allRoleUsers.forEach(roleUser -> {
            if (roleUser.getChangeRequestWorkflowDetailId() != null) {
                ChangeRequestWorkflowDetailModel detailModel =
                        workflowDetailModelMap.get(roleUser.getChangeRequestWorkflowDetailId());
                if (detailModel != null) {
                    roleUser.setChangeRequestWorkflowId(detailModel.getChangeRequestWorkflowId());
                    roleUser.setChangeNodeName(detailModel.getChangeNodeModel().getNodeName());
                    roleUser.setChangeRequestWorkflowName(
                            detailModel.getChangeRequestWorkflowModel().getName());
                    roleUser.setChangeNodeId(detailModel.getChangeNodeModel().getId());
                }
            }
        });


        for (ChangeRequestRoleModel item : items) {
            List<ChangeRequestRoleUserModel> cabUsers = item.getUsers();
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


            // create map of change request workflow detail id to ch


            List<ChangeRequestRoleWorkflowListModel> workflowUsersList = new ArrayList<>();
            for (Map.Entry<Long, List<ChangeRequestRoleUserModel>> workflowEntry : groupedByWorkflow.entrySet()) {
                List<ChangeRequestRoleUserModel> usersInCurrentWorkflow = workflowEntry.getValue();

                Map<Integer, List<ChangeRequestRoleUserModel>> groupedByCabGroup = new TreeMap<>();

                //add logic handle null cab group, i want group null cab group to cab group 0

                groupedByCabGroup = usersInCurrentWorkflow.stream().collect(
                        Collectors.groupingBy(ChangeRequestRoleUserModel::getCabGroup,
                                () -> new TreeMap<>(Comparator.naturalOrder()),
                                Collectors.toList()));

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


                workflowUsersList.add(ChangeRequestRoleWorkflowListModel.builder()
                        .changeWorkflowId(workflowEntry.getKey())
                        .changeWorkflowName(mapWorkflows.get(workflowEntry.getKey()).getName())
                        .groups(cabUserListModels).build());
            }
            item.setWorkflows(workflowUsersList);
        }

        return items;
    }


    @Override
    public List<ChangeRequestRoleModel> getOrDefaultAllChangeFlowNodesByChangeTemplateIdAndRequestId(
            Long changeTemplateId, Long changeRequestId) throws BusinessException {

        var isNotSelectChangeTemplate = changeTemplateId == null || changeTemplateId == 0L;
        if (isNotSelectChangeTemplate) {
            return Collections.emptyList();
        }
        var defaultCreateChangeId = 0L;
        if (changeRequestId == null) {
            changeRequestId = defaultCreateChangeId;
        }

        List<ChangeFlowNodeModel> flowNodeModelList =
                changeFlowNodeService.findAllChangeFlowNodesByChangeTemplateId(changeTemplateId);
        Map<Long, ChangeFlowNodeModel> mapFlowNode = flowNodeModelList.stream()
                .collect(Collectors.toMap(ChangeFlowNodeModel::getId, Function.identity()));

        List<ChangeRequestRoleModel> requestRoleLst = new ArrayList<>();
        Map<Long, List<ChangeRequestRoleUserModel>> mapRoleIdWithUsers = new HashMap<>();

        if (changeRequestId != defaultCreateChangeId) {
            requestRoleLst = changeRequestRoleMapper.mapTo(
                            repository.findAllByChangeRequestId(changeRequestId)).stream()
                    .filter(role -> Objects.nonNull(role.getChangeFlowNodeId()) &&
                            mapFlowNode.containsKey(role.getChangeFlowNodeId()))
                    .peek(role -> role.setChangeFlowNode(
                            mapFlowNode.get(role.getChangeFlowNodeId()))).toList();

            List<ChangeRequestRoleUserModel> users =
                    changeRequestRoleUserService.findAllByChangeRequestId(changeRequestId);
            mapRoleIdWithUsers = users.stream().collect(
                    Collectors.groupingBy(ChangeRequestRoleUserModel::getChangeRequestRoleId));
            Map<Long, List<ChangeRequestRoleUserModel>> finalMapRoleIdWithUsers =
                    mapRoleIdWithUsers;
            requestRoleLst = requestRoleLst.stream()
                    .peek(role -> role.setUsers(finalMapRoleIdWithUsers.get(role.getId())))
                    .toList();
        }
        Map<Long, ChangeRequestRoleModel> mapNodeWithRole = requestRoleLst.stream().collect(
                Collectors.toMap(ChangeRequestRoleModel::getChangeFlowNodeId, Function.identity()));
        var allRoleUsers =
                mapRoleIdWithUsers.values().stream().flatMap(Collection::stream).toList();
        var changeRequestWorkflowDetailIds = allRoleUsers.stream()
                .map(ChangeRequestRoleUserModel::getChangeRequestWorkflowDetailId)
                .filter(Objects::nonNull).distinct().toList();
        var workflowDetailList =
                changeRequestWorkflowDetailService.findAllByChangeRequestId(changeRequestId);
        var workflowDetailModelMap = workflowDetailList.stream().collect(
                Collectors.toMap(ChangeRequestWorkflowDetailModel::getId, Function.identity()));
        //create map of change request workflow id by model in workflowDetailList
        var mapWorkflows = changeRequestWorkflowService.getMapChangeWorkflowByIds(
                workflowDetailList.stream()
                        .map(ChangeRequestWorkflowDetailModel::getChangeRequestWorkflowId)
                        .distinct().toList());

        allRoleUsers.forEach(roleUser -> {
            if (roleUser.getChangeRequestWorkflowDetailId() != null) {
                ChangeRequestWorkflowDetailModel detailModel =
                        workflowDetailModelMap.get(roleUser.getChangeRequestWorkflowDetailId());
                if (detailModel != null) {
                    roleUser.setChangeRequestWorkflowId(detailModel.getChangeRequestWorkflowId());
                    roleUser.setChangeNodeName(detailModel.getChangeNodeModel().getNodeName());
                    roleUser.setChangeRequestWorkflowName(
                            detailModel.getChangeRequestWorkflowModel().getName());
                    roleUser.setChangeNodeId(detailModel.getChangeNodeModel().getId());
                }
            }
        });

        AtomicInteger index = new AtomicInteger(flowNodeModelList.size());
        Map<Long, List<ChangeRequestRoleUserModel>> finalUsersByChangeRequestRoleId =
                mapRoleIdWithUsers;
        Long finalChangeRequestId = changeRequestId;
        requestRoleLst = flowNodeModelList.stream().map((flowNode) -> {
            if (mapNodeWithRole.containsKey(flowNode.getId())) {
                return mapNodeWithRole.get(flowNode.getId());
            }
            ChangeRequestRoleModel role =
                    ChangeRequestRoleModel.builder().id((long) index.getAndDecrement())
                            .changeFlowNodeId(flowNode.getId()).changeFlowNode(flowNode)
                            .changeRequestId(finalChangeRequestId).changeFlowNode(flowNode).build();

            if (ChangeFlowNodeTypeEnum.CAB.equals(flowNode.getType())) {

            }
            finalUsersByChangeRequestRoleId.put(role.getId(),
                    List.of(ChangeRequestRoleUserModel.builder().username(Strings.EMPTY).id(0L)
                            .changeRequestRoleId(role.getId()).build()));
            return role;
        }).toList();

        return getWorkflowAndGroup(requestRoleLst, mapWorkflows);
    }


    @Override
    public List<ChangeRequestRoleModel> getWorkflowAndGroup(List<ChangeRequestRoleModel> roles,
                                                            Map<Long, ChangeRequestWorkflowModel> mapWorkflows)
            throws BusinessException {
        if (roles == null || roles.isEmpty()) {
            return roles;
        }


        for (ChangeRequestRoleModel role : roles) {
            List<ChangeRequestRoleUserModel> cabUsers = role.getUsers();
            if (cabUsers == null || cabUsers.isEmpty()) {
                role.setWorkflows(Collections.emptyList());
                continue;
            }


            var groupedByWorkflow = cabUsers.stream().collect(
                    Collectors.groupingBy(ChangeRequestRoleUserModel::getChangeRequestWorkflowId));
            List<ChangeRequestRoleWorkflowListModel> workflowUsersList = new ArrayList<>();
            for (Map.Entry<Long, List<ChangeRequestRoleUserModel>> workflowEntry : groupedByWorkflow.entrySet()) {
                var usersInCurrentWorkflow = workflowEntry.getValue();

                var groupedByCabGroup = usersInCurrentWorkflow.stream().collect(
                        Collectors.groupingBy(ChangeRequestRoleUserModel::getCabGroup,
                                () -> new TreeMap<>(Comparator.naturalOrder()),
                                Collectors.toList()));

                var sorted2dList = new ArrayList<List<ChangeRequestRoleUserModel>>();
                for (Map.Entry<Integer, List<ChangeRequestRoleUserModel>> entry : groupedByCabGroup.entrySet()) {
                    var group = entry.getValue();
                    var sortedGroup = group.stream().sorted(Comparator.comparing(
                                    ChangeRequestRoleUserModel::getCabGroupOrder,
                                    Comparator.nullsLast(Comparator.naturalOrder())))
                            .collect(Collectors.toList());
                    sorted2dList.add(sortedGroup);
                }
                List<ChangeRequestRoleUserListModel> cabUserListModels =
                        sorted2dList.stream().map(oneGroup -> {
                            String groupName = null;
                            if (!KanbanCommonUtil.listIsEmptyOrNull(oneGroup)) {
                                groupName = oneGroup.get(0).getCabGroupName();
                            }

                            return ChangeRequestRoleUserListModel.builder().users(oneGroup)
                                    .cabGroupName(groupName).build();
                        }).collect(Collectors.toList());


                workflowUsersList.add(ChangeRequestRoleWorkflowListModel.builder()
                        .changeWorkflowId(workflowEntry.getKey())
                        .changeWorkflowName(mapWorkflows.get(workflowEntry.getKey()).getName())
                        .groups(cabUserListModels).build());
            }
            role.setWorkflows(workflowUsersList);
        }

        return roles;
    }
}
