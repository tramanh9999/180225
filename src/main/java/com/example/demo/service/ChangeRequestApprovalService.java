package com.example.demo.service;

import com.example.demo.model.ChangeRequestApprovalModel;

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
}
