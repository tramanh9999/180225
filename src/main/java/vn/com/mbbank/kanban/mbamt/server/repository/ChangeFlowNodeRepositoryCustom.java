package vn.com.mbbank.kanban.mbamt.server.repository;

import vn.com.mbbank.kanban.mbamt.server.enums.ChangeFlowNodeType;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeFlowNodeModel;
import vn.com.mbbank.kanban.mbamt.server.model.PagingRequestModel;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Custom repository interface for ChangeFlowNodeEntity.
 */
public interface ChangeFlowNodeRepositoryCustom {


    Page<String> findUsernamesByChangeFlowNodeId(Long changeFlowNodeId,
                                                 PagingRequestModel requestModel);

    long countUsersByChangeFlowNodeIdNative(Long changeFlowNodeId);


    List<ChangeFlowNodeModel> findChangeFlowNodesByTemplateIdAndTypeIn(Long changeTemplateId,
                                                                       List<ChangeFlowNodeType> types);
}