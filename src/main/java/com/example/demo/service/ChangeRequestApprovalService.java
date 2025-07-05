package com.example.demo.service;

import com.example.demo.model.ChangeRequestApprovalModel;
import com.example.demo.model.ChangeRequestRoleModel;

import java.util.List;

/**
 * The interface Change request approval service.
 */
public interface ChangeRequestApprovalService {

    /**
     * Find by id change request approval model.
     *
     * @param id the id
     * @return the change request approval model
     */
    ChangeRequestApprovalModel findById(Long id);

    /**
     * Find all list.
     *
     * @return the list
     */
    List<ChangeRequestApprovalModel> findAll();

    /**
     * Save list list.
     *
     * @param approvalDto the approval dto
     * @return the list
     */
    List<ChangeRequestApprovalModel> saveList(List<ChangeRequestApprovalModel> approvalDto);

    /**
     * Delete by id.
     *
     * @param id the id
     */
    void deleteById(Long id);

    /**
     * Find by change request id list.
     *
     * @param changeRequestId the change request id
     * @return the list
     */
    List<ChangeRequestApprovalModel> findByChangeRequestId(Long changeRequestId);

    /**
     * Find by overall status list.
     *
     * @param status the status
     * @return the list
     */
    List<ChangeRequestApprovalModel> findByOverallStatus(String status);

    /**
     * Find by change request id and overall status list.
     *
     * @param changeRequestId the change request id
     * @param status          the status
     * @return the list
     */
    List<ChangeRequestApprovalModel> findByChangeRequestIdAndOverallStatus(Long changeRequestId,
                                                                           String status);

    /**
     * Find by change flow id and node id change request role model.
     *
     * @param changeRequestId     the change request id
     * @param changeFlowId        the change flow id
     * @param changeFlowNodeStrId the change flow node str id
     * @return the change request role model
     */
    ChangeRequestRoleModel findByChangeFlowIdAndNodeId(Long changeRequestId, Long changeFlowId,
                                                       String changeFlowNodeStrId);

    /**
     * Find by change request role user id in list.
     *
     * @param roleUserIds the role user ids
     * @return the list
     */
    List<ChangeRequestApprovalModel> findByChangeRequestRoleUserIdIn(List<Long> roleUserIds);


    /**
     * Save change request approval model.
     *
     * @param replyModel the reply model
     * @return the change request approval model
     */
    ChangeRequestApprovalModel save(ChangeRequestApprovalModel replyModel);
}
