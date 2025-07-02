package com.example.demo.service;

import com.example.demo.model.ChangeRequestApprovalModel;
import com.example.demo.model.ChangeRequestRoleModel;

import java.util.List;

public interface ChangeRequestApprovalService {

    ChangeRequestApprovalModel findById(Long id);

    List<ChangeRequestApprovalModel> findAll();

    List<ChangeRequestApprovalModel> saveList(List<ChangeRequestApprovalModel> approvalDto);

    void deleteById(Long id);

    List<ChangeRequestApprovalModel> findByChangeRequestId(Long changeRequestId);

    List<ChangeRequestApprovalModel> findByOverallStatus(String status);

    List<ChangeRequestApprovalModel> findByChangeRequestIdAndOverallStatus(Long changeRequestId,
                                                                           String status);

    ChangeRequestRoleModel findByChangeFlowIdAndNodeId(Long changeRequestId, Long changeFlowId,
                                                       String changeFlowNodeStrId);

    List<ChangeRequestApprovalModel> findByChangeRequestRoleUserIdIn(List<Long> roleUserIds);


    ChangeRequestApprovalModel save(ChangeRequestApprovalModel replyModel);
}
