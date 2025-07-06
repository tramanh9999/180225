package vn.com.mbbank.kanban.mbamt.server.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestRoleEntity;
import vn.com.mbbank.kanban.mbamt.server.enums.ChangeFlowNodeTypeEnum;
import vn.com.mbbank.kanban.mbamt.server.mapper.ChangeRequestRoleMapper;
import vn.com.mbbank.kanban.mbamt.server.mapper.ChangeRequestRoleUserMapper;
import vn.com.mbbank.kanban.mbamt.server.model.*;
import vn.com.mbbank.kanban.mbamt.server.repository.ChangeRequestRoleRepository;
import vn.com.mbbank.kanban.mbamt.server.repository.ChangeRequestService;
import vn.com.mbbank.kanban.mbamt.server.service.*;
import vn.com.mbbank.kanban.mbamt.server.utils.KanbanCommonUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static vn.com.mbbank.kanban.mbamt.server.constants.ChangeRoleConstants.CAB_GROUP_FOR_WORKFLOW_NODE_APPROVAL;

/**
 * Implementation of ChangeRequestRoleService for managing Change Request Roles.
 */
@Service
@RequiredArgsConstructor
@Slf4j
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
    @Autowired
    private ChangeRequestRoleUserMapper changeRequestRoleUserMapper;


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

            //            if (roleModel.getUsers() != null && !roleModel.getUsers().isEmpty()) {
            //                roleModel.getUsers().forEach(userModel -> {
            //                    userModel.setChangeRequestRoleId(savedRoleEntity.getId());
            //                    userModel.setId(null);
            //                    allUserModelsToSave.add(userModel);
            //                });
            //            }
        }
        if (!allUserModelsToSave.isEmpty()) {
            changeRequestRoleUserService.saveAll(allUserModelsToSave);
        }
    }

    @Override
    public void validateList(List<ChangeRequestRoleModel> roles) {
        if (roles == null || roles.isEmpty()) {
            return;
        }

        Map<Long, ChangeFlowNodeModel> changeFlowNodeModelMap = changeFlowNodeService.findByIdIn(
                roles.stream().map(ChangeRequestRoleModel::getChangeFlowNodeId).toList());

        for (ChangeRequestRoleModel role : roles) {

            if (!changeFlowNodeModelMap.containsKey(role.getChangeFlowNodeId())) {
                throw new BusinessException(ErrorCodeCommon.CHANGE_FLOW_NODE_ID_NOT_FOUND,
                        role.getChangeFlowNodeId());
            }

        }

        //todo validate username in list group user
        changeRequestRoleUserService.validateList(roles.stream().flatMap(
                role -> role.getWorkflows().stream().flatMap(
                        workflow -> workflow.getGroups().stream()
                                .flatMap(group -> group.getUsers().stream()))).toList());
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
                findAndSyncChangeFlowAndWorflowsApprovalChangeRequestRolesByChangeRequestId(
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


    //NEW

    @Override
    public List<ChangeRequestRoleModel> findAndSyncChangeFlowAndWorflowsApprovalChangeRequestRolesByChangeRequestId(
            Long changeRequestId) throws BusinessException {
        var changeRequest = changeRequestService.findById(changeRequestId);
        var changeTemplateId = changeRequest.getChangeTemplateId();

        //role user
        var allChangeRequestRoleUsers =
                changeRequestRoleUserService.findAllByChangeRequestId(changeRequestId);
        var mapAllRoleIdWithUsers = allChangeRequestRoleUsers.stream()
                .collect(Collectors.groupingBy(ChangeRequestRoleUserModel::getChangeRequestRoleId));


        //workflow
        var newestChangeWorkflows =
                changeRequestWorkflowService.findAllByChangeRequestId(changeRequestId);
        var mapNewestWorkflow = newestChangeWorkflows.stream()
                .collect(Collectors.toMap(ChangeRequestWorkflowModel::getId, Function.identity()));


        //workflow details
        var mapNewestWorkflowDetails =
                changeRequestWorkflowDetailService.findAllByChangeRequestId(changeRequestId)
                        .stream().collect(Collectors.groupingBy(
                                ChangeRequestWorkflowDetailModel::getChangeRequestWorkflowId));

        //role
        var requestRoleLst =
                changeRequestRoleMapper.mapTo(repository.findAllByChangeRequestId(changeRequestId));
        var nodeIdSet = requestRoleLst.stream().map(ChangeRequestRoleModel::getChangeFlowNodeId)
                .collect(Collectors.toSet());
        var mapNodeIdAndRole = requestRoleLst.stream().collect(
                Collectors.toMap(ChangeRequestRoleModel::getChangeFlowNodeId, Function.identity()));
        var newestNodeList =
                changeFlowNodeService.findAllChangeFlowNodesByChangeTemplateId(changeTemplateId);
        var roleIdNew = new AtomicInteger(0);
        return new ArrayList<>(newestNodeList.stream().map(node -> {
            if (!nodeIdSet.contains(node.getId())) {
                long decrementRoleId = roleIdNew.getAndDecrement();
                return initNewRole(node, decrementRoleId, newestChangeWorkflows,
                        mapNewestWorkflowDetails, changeRequestId);
            } else {
                return getOldRole(node, mapNodeIdAndRole, mapNewestWorkflow, mapAllRoleIdWithUsers,
                        mapNewestWorkflowDetails, changeRequestId);
            }
        }).toList());
    }

    @Override
    public ChangeRequestRoleModel initNewRole(ChangeFlowNodeModel node,
                                              long defaultRoleIdAutoDecrement,
                                              List<ChangeRequestWorkflowModel> allChangeWorkflows,
                                              Map<Long, List<ChangeRequestWorkflowDetailModel>> mapAllNewestWorkflowDetails,
                                              Long changeRequestId) {
        var defaultUsers = List.of(ChangeRequestRoleUserModel.builder().id(0L)
                .changeRequestRoleId(defaultRoleIdAutoDecrement).username(Strings.EMPTY).build());

        var workflowListModel =
                List.of(ChangeRequestRoleWorkflowListModel.builder().changeWorkflowId(0L)
                        .changeWorkflowName("No need workflow").groups(List.of(
                                ChangeRequestRoleUserListModel.builder()
                                        .cabGroupName("No need group").users(defaultUsers).build()))
                        .build());

        if (ChangeFlowNodeTypeEnum.CAB.equals(node.getType())) {
            workflowListModel = new ArrayList<>(allChangeWorkflows.stream()
                    .map(workflow -> initWorkflowInCabRole(workflow.getId(), workflow.getName(),
                            workflow.getId(),
                            mapAllNewestWorkflowDetails.getOrDefault(workflow.getId(), List.of())))
                    .toList());
        }
        return ChangeRequestRoleModel.builder().id(defaultRoleIdAutoDecrement)
                .changeRequestId(changeRequestId).changeFlowNodeId(node.getId())
                .changeFlowNode(node).workflows(workflowListModel).build();
    }

    @Override
    public ChangeRequestRoleModel getOldRole(ChangeFlowNodeModel node,
                                             Map<Long, ChangeRequestRoleModel> mapNodeWithCreatedRole,
                                             Map<Long, ChangeRequestWorkflowModel> mapNewestWorkflow,
                                             Map<Long, List<ChangeRequestRoleUserModel>> mapAllRoleIdWithUsers,
                                             Map<Long, List<ChangeRequestWorkflowDetailModel>> mapWorkflowIdWithDetails,
                                             Long changeRequestId) {

        //START Normal role.
        var role = mapNodeWithCreatedRole.get(node.getId());
        Long roleId = role.getId();

        if (ChangeFlowNodeTypeEnum.APPROVAL.equals(node.getType())) {

            var oldRoleUsers = mapAllRoleIdWithUsers.get(roleId);

            return ChangeRequestRoleModel.builder().id(roleId).changeRequestId(changeRequestId)
                    .changeFlowNodeId(roleId).changeFlowNode(node)
                    .workflows(initWorkflowVirtualInApprovalRole(roleId, oldRoleUsers)).build();
        }


        var cabRole = new ChangeRequestRoleModel();
        var mapWfRoleUsers = mapAllRoleIdWithUsers.getOrDefault(roleId, List.of()).stream().collect(
                Collectors.groupingBy(ChangeRequestRoleUserModel::getChangeRequestWorkflowId,
                        Collectors.toList()));
        cabRole.getWorkflows().addAll(mapNewestWorkflow.entrySet().stream().map(workflow -> {
            Long workflowId = workflow.getKey();
            var oldRoleUsers = mapWfRoleUsers.getOrDefault(workflowId, List.of());
            var newDetailsSortId =
                    mapWorkflowIdWithDetails.getOrDefault(workflowId, List.of()).stream()
                            .sorted(Comparator.comparing(ChangeRequestWorkflowDetailModel::getId))
                            .toList();

            //case workflow  don't have role users in role (new workflow/ init change)
            if (KanbanCommonUtil.listIsEmptyOrNull(oldRoleUsers)) {

                return initWorkflowInCabRole(workflowId, workflow.getValue().getName(), roleId,
                        newDetailsSortId);
            }

            //case workflow has role users in role
            var mapNewestDetails = newDetailsSortId.stream().collect(
                    Collectors.toMap(ChangeRequestWorkflowDetailModel::getId, Function.identity()));
            var oldRoleUserWithWorkflowDetailsAndCustomRoleUsers = oldRoleUsers.stream()
                    .filter(user -> user.getChangeRequestWorkflowDetailId() == null ||
                            mapNewestDetails.containsKey(user.getChangeRequestWorkflowDetailId()))
                    .toList();
            var oldUserWithWorkflowDetails = oldRoleUsers.stream()
                    .filter(user -> user.getChangeRequestWorkflowDetailId() != null &&
                            mapNewestDetails.containsKey(user.getChangeRequestWorkflowDetailId()))
                    .toList();
            var mapOldDetailWithUser = oldUserWithWorkflowDetails.stream().collect(
                    Collectors.toMap(ChangeRequestRoleUserModel::getChangeRequestWorkflowDetailId,
                            Function.identity()));
            var oldGroupUserList =
                    orderAndGroupApprovalUsers(oldRoleUserWithWorkflowDetailsAndCustomRoleUsers);
            var groupForNewCabsFromWorkflow =
                    oldGroupUserList.get(CAB_GROUP_FOR_WORKFLOW_NODE_APPROVAL);
            var newDetails = mapNewestDetails.entrySet().stream()
                    .filter(entry -> !mapOldDetailWithUser.containsKey(entry.getKey()))
                    .map(Map.Entry::getValue).toList();
            var newRoleUserWithDetails = initNodeApprovalPositionsInCabRole(newDetails, roleId,
                    groupForNewCabsFromWorkflow.getUsers().size());
            groupForNewCabsFromWorkflow.getUsers().addAll(newRoleUserWithDetails);
            return ChangeRequestRoleWorkflowListModel.builder().changeWorkflowId(workflowId)
                    .changeWorkflowName(workflow.getValue().getName()).groups(oldGroupUserList)
                    .build();
        }).toList());
        return cabRole;
    }

    @Override
    public List<ChangeRequestRoleWorkflowListModel> initWorkflowVirtualInApprovalRole(Long roleId,
                                                                                      List<ChangeRequestRoleUserModel> oldRoleUsers) {

        var defaultUsers = oldRoleUsers;
        if (KanbanCommonUtil.listIsEmptyOrNull(oldRoleUsers)) {
            defaultUsers =
                    List.of(ChangeRequestRoleUserModel.builder().id(0L).changeRequestRoleId(roleId)
                            .username(Strings.EMPTY).build());
        }
        return new ArrayList<>(
                List.of(ChangeRequestRoleWorkflowListModel.builder().changeWorkflowId(0L)
                        .changeWorkflowName("Workflow for normal role").groups(List.of(
                                ChangeRequestRoleUserListModel.builder()
                                        .cabGroupName("Group for normal role").users(defaultUsers)
                                        .build())).build()));


    }

    /**
     * Initialize workflow node role users for CAB role.
     *
     * @param workflowId     the ID of the workflow
     * @param workflowName   the name of the workflow
     * @param roleId         the ID of the role (usually CAB role)
     * @param orderedDetails the list of workflow details
     * @return a ChangeRequestRoleWorkflowListModel initialized with the details
     */
    @Override
    public ChangeRequestRoleWorkflowListModel initWorkflowInCabRole(Long workflowId,
                                                                    String workflowName,
                                                                    Long roleId,
                                                                    List<ChangeRequestWorkflowDetailModel> orderedDetails) {


        var workflowNodeUsers = initNodeApprovalPositionsInCabRole(orderedDetails, roleId, 0);
        return ChangeRequestRoleWorkflowListModel.builder().changeWorkflowId(workflowId)
                .changeWorkflowName(workflowName).groups(List.of(
                        ChangeRequestRoleUserListModel.builder()
                                .cabGroupName("CAB Group for nodes in workflow id " + workflowId)
                                .users(workflowNodeUsers).build())).build();


    }

    /**
     * Initialize node approval positions in CAB role.
     *
     * @param orderedDetails      the list of ordered workflow details
     * @param changeRequestRoleId the ID of the change request role
     * @param orderApprovalStart  the starting order for approval
     * @return a list of ChangeRequestRoleUserModel initialized with CAB group and order
     */
    @Override
    public List<ChangeRequestRoleUserModel> initNodeApprovalPositionsInCabRole(
            List<ChangeRequestWorkflowDetailModel> orderedDetails, Long changeRequestRoleId,
            int orderApprovalStart) {
        if (KanbanCommonUtil.listIsEmptyOrNull(orderedDetails)) {
            return List.of();
        }
        return orderedDetails.stream().map((detail) -> ChangeRequestRoleUserModel.builder()
                .username("Setting cabUser in " + detail.getId())
                .cabGroup(CAB_GROUP_FOR_WORKFLOW_NODE_APPROVAL)
                // index of detail in workflow
                .cabGroupName("CAB Group for workflow node detail id " + detail.getId())
                .cabGroupOrder(orderedDetails.indexOf(detail) + orderApprovalStart)
                .changeRequestWorkflowId(detail.getChangeRequestWorkflowId())
                .changeRequestRoleId(changeRequestRoleId).build()).toList();
    }


    /**
     * Order and group approval users by their CAB group and CAB group order.
     *
     * @param disorderlyUsers the list of ChangeRequestRoleUserModel to be ordered and grouped
     * @return a list of ChangeRequestRoleUserListModel containing grouped users
     */
    @Override
    public List<ChangeRequestRoleUserListModel> orderAndGroupApprovalUsers(
            List<ChangeRequestRoleUserModel> disorderlyUsers) {

        if (KanbanCommonUtil.listIsEmptyOrNull(disorderlyUsers)) {
            return List.of();
        }
        var groupedByCabGroup = disorderlyUsers.stream().collect(
                Collectors.groupingBy(ChangeRequestRoleUserModel::getCabGroup,
                        () -> new TreeMap<>(Comparator.naturalOrder()), Collectors.toList()));

        var sorted2dList = new ArrayList<List<ChangeRequestRoleUserModel>>();
        for (Map.Entry<Integer, List<ChangeRequestRoleUserModel>> entry : groupedByCabGroup.entrySet()) {
            var group = entry.getValue();
            var sortedGroup = group.stream()
                    .sorted(Comparator.comparing(ChangeRequestRoleUserModel::getCabGroupOrder,
                            Comparator.naturalOrder())).collect(Collectors.toList());
            sorted2dList.add(sortedGroup);
        }

        return sorted2dList.stream().map(oneGroup -> {
            String groupName = null;
            if (!KanbanCommonUtil.listIsEmptyOrNull(oneGroup)) {
                groupName = oneGroup.get(0).getCabGroupName();
            }
            return ChangeRequestRoleUserListModel.builder().users(oneGroup).cabGroupName(groupName)
                    .build();
        }).collect(Collectors.toList());
    }
}
