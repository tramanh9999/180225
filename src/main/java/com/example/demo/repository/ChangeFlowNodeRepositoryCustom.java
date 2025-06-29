package com.example.demo.repository;

import com.example.demo.model.ChangeFlowNodeModel;
import com.example.demo.model.PagingRequestModel;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Custom repository interface for ChangeFlowNodeEntity.
 */
public interface ChangeFlowNodeRepositoryCustom {


    Page<String> findUsernamesByChangeFlowNodeId(Long changeFlowNodeId,
                                                 PagingRequestModel requestModel);

    long countUsersByChangeFlowNodeIdNative(Long changeFlowNodeId);

    List<ChangeFlowNodeModel> findChangeFlowNodesByTemplateId(Long changeTemplateId);
}