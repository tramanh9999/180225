package vn.com.mbbank.kanban.mbamt.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import vn.com.mbbank.kanban.mbamt.server.entity.ChangeNodeEntity;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeWorkflowNodeModel;

import java.util.List;

/**
 * Mapper for converting between ChangeNodeEntity and ChangeNodeModel.
 */
@Mapper(componentModel = "spring")
@Component
public interface ChangeNodeMapper {

    /**
     * Convert a list of ChangeNodeEntity to a list of ChangeNodeModel.
     *
     * @param entities the entities to convert
     * @return the converted models
     */
    List<ChangeWorkflowNodeModel> toDtoList(List<ChangeNodeEntity> entities);

    ChangeWorkflowNodeModel cloneModel(ChangeWorkflowNodeModel model);


    List<ChangeWorkflowNodeModel> toModels(List<ChangeNodeEntity> entities);

    ChangeWorkflowNodeModel toModel(ChangeNodeEntity entity);
}