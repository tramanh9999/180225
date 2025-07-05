package vn.com.mbbank.kanban.mbamt.server.repository;

import org.springframework.data.domain.Page;
import vn.com.mbbank.kanban.mbamt.server.enums.ChangeFlowNodeTypeEnum;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeFlowNodeModel;
import vn.com.mbbank.kanban.mbamt.server.model.PagingRequestModel;

import java.util.List;

/**
 * Custom repository interface for ChangeFlowNodeEntity.
 */
public interface ChangeFlowNodeRepositoryCustom {


    Page<String> findUsernamesByChangeFlowNodeId(Long changeFlowNodeId,
                                                 PagingRequestModel requestModel);

    long countUsersByChangeFlowNodeIdNative(Long changeFlowNodeId);


    List<ChangeFlowNodeModel> findChangeFlowNodesByTemplateIdAndTypeIn(Long changeTemplateId,
                                                                       List<ChangeFlowNodeTypeEnum> types);
}