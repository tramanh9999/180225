package vn.com.mbbank.kanban.mbamt.server.mapper;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestWorkflowDetailEntity;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestWorkflowDetailModel;
import org.springframework.stereotype.Component;

@Component
public class ChangeRequestWorkflowDetailMapper {

    public ChangeRequestWorkflowDetailModel toModel(ChangeRequestWorkflowDetailEntity entity) {
        if (entity == null) {
            return null;
        }
        return ChangeRequestWorkflowDetailModel.builder().id(entity.getId())
                .changeRequestWorkflowId(entity.getChangeRequestWorkflowId())
                .changeNodeId(entity.getChangeNodeId()).build();
    }

    public ChangeRequestWorkflowDetailEntity toEntity(ChangeRequestWorkflowDetailModel model) {
        if (model == null) {
            return null;
        }
        return ChangeRequestWorkflowDetailEntity.builder()
                .id(model.getId()) // ID might be null for new entities
                .changeRequestWorkflowId(model.getChangeRequestWorkflowId())
                .changeNodeId(model.getChangeNodeId()).build();
    }

    // You might also need methods for updating an existing entity from a model
    public void updateEntityFromModel(ChangeRequestWorkflowDetailModel model,
                                      ChangeRequestWorkflowDetailEntity entity) {
        if (model == null || entity == null) {
            return;
        }
        // entity.setId(model.getId()); // Don't update ID if it's auto-generated
        entity.setChangeRequestWorkflowId(model.getChangeRequestWorkflowId());
        entity.setChangeNodeId(model.getChangeNodeId());
    }
}