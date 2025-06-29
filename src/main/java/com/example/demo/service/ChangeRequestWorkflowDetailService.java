package com.example.demo.service;

import com.example.demo.model.ChangeRequestWorkflowDetailModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ChangeRequestWorkflowDetailService {

    Optional<ChangeRequestWorkflowDetailModel> getDetailById(Long id);

    @Transactional(readOnly = true)
    List<ChangeRequestWorkflowDetailModel> getDetailsByIds(List<Long> ids);

    List<ChangeRequestWorkflowDetailModel> getAllDetailsByWorkflowId(Long changeRequestWorkflowId);

    ChangeRequestWorkflowDetailModel createDetail(ChangeRequestWorkflowDetailModel detailModel);

    ChangeRequestWorkflowDetailModel updateDetail(ChangeRequestWorkflowDetailModel detailModel);

    void deleteDetail(Long id);

    Optional<ChangeRequestWorkflowDetailModel> getDetailByWorkflowIdAndChangeNodeId(
            Long changeRequestWorkflowId, Long changeNodeId);
}