package vn.com.mbbank.kanban.mbamt.server.service;

import jakarta.transaction.Transactional;
import vn.com.mbbank.kanban.mbamt.server.model.*;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing Change Request Roles.
 */
public interface ChangeRequestRoleService {
    /**
     * Save list.
     *
     * @param changeRequestId the change request id
     * @param roles           the roles
     */
    @Transactional
    void saveList(Long changeRequestId, List<ChangeRequestRoleModel> roles);


    /**
     * Validate a list of Change Request Role models.
     * Checks if required fields are present and if referenced entities exist.
     *
     * @param roles The list of Change Request Role models to validate
     * @throws BusinessException if validation fails
     */
    void validateList(List<ChangeRequestRoleModel> roles);


    /**
     * Gets change request role by change flow node id.
     *
     * @param changeRequestId  the change request id
     * @param changeTemplateId the change template id
     * @param changeFlowNodeId the change flow node id
     * @return the change request role by change flow node id
     */
    ChangeRequestRoleModel getChangeRequestRoleByChangeFlowNodeId(Long changeRequestId,
                                                                  Long changeTemplateId,
                                                                  Long changeFlowNodeId);


    /**
     * Find and sync change flow and worflows approval change request roles by change request id list.
     *
     * @param changeRequestId the change request id
     * @return the list
     * @throws BusinessException the business exception
     */
    List<ChangeRequestRoleModel> findAndSyncChangeFlowAndWorflowsApprovalChangeRequestRolesByChangeRequestId(
            Long changeRequestId) throws BusinessException;

    /**
     * Init new role change request role model.
     *
     * @param node                        the node
     * @param defaultRoleIdAutoDecrement  the default role id auto decrement
     * @param allChangeWorkflows          the all change workflows
     * @param mapAllNewestWorkflowDetails the map all newest workflow details
     * @param changeRequestId             the change request id
     * @return the change request role model
     */
    ChangeRequestRoleModel initNewRole(ChangeFlowNodeModel node, long defaultRoleIdAutoDecrement,
                                       List<ChangeRequestWorkflowModel> allChangeWorkflows,
                                       Map<Long, List<ChangeRequestWorkflowDetailModel>> mapAllNewestWorkflowDetails,
                                       Long changeRequestId);

    /**
     * Gets old role.
     *
     * @param node                     the node
     * @param mapNodeWithCreatedRole   the map node with created role
     * @param mapNewestWorkflow        the map newest workflow
     * @param mapAllRoleIdWithUsers    the map all role id with users
     * @param mapWorkflowIdWithDetails the map workflow id with details
     * @param changeRequestId          the change request id
     * @return the old role
     */
    ChangeRequestRoleModel getOldRole(ChangeFlowNodeModel node,
                                      Map<Long, ChangeRequestRoleModel> mapNodeWithCreatedRole,
                                      Map<Long, ChangeRequestWorkflowModel> mapNewestWorkflow,
                                      Map<Long, List<ChangeRequestRoleUserModel>> mapAllRoleIdWithUsers,
                                      Map<Long, List<ChangeRequestWorkflowDetailModel>> mapWorkflowIdWithDetails,
                                      Long changeRequestId);

    /**
     * Init workflow virtual in approval role list.
     *
     * @param roleId       the role id
     * @param oldRoleUsers the old role users
     * @return the list
     */
    List<ChangeRequestRoleWorkflowListModel> initWorkflowVirtualInApprovalRole(Long roleId,
                                                                               List<ChangeRequestRoleUserModel> oldRoleUsers);

    /**
     * Init workflow in cab role change request role workflow list model.
     *
     * @param workflowId     the workflow id
     * @param workflowName   the workflow name
     * @param roleId         the role id
     * @param orderedDetails the ordered details
     * @return the change request role workflow list model
     */
    ChangeRequestRoleWorkflowListModel initWorkflowInCabRole(Long workflowId, String workflowName,
                                                             Long roleId,
                                                             List<ChangeRequestWorkflowDetailModel> orderedDetails);

    /**
     * Init node approval positions in cab role list.
     *
     * @param orderedDetails      the ordered details
     * @param changeRequestRoleId the change request role id
     * @param orderApprovalStart  the order approval start
     * @return the list
     */
    List<ChangeRequestRoleUserModel> initNodeApprovalPositionsInCabRole(
            List<ChangeRequestWorkflowDetailModel> orderedDetails, Long changeRequestRoleId,
            int orderApprovalStart);

    /**
     * Order and group approval users list.
     *
     * @param disorderlyUsers the disorderly users
     * @return the list
     */
    List<ChangeRequestRoleUserListModel> orderAndGroupApprovalUsers(
            List<ChangeRequestRoleUserModel> disorderlyUsers);
}
