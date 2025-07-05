package vn.com.mbbank.kanban.mbamt.server.mapper;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeFlowNodeEntity;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeFlowNodeModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper for converting between ChangeFlowNodeEntity and ChangeFlowNodeModel.
 */
@Mapper(componentModel = "spring")
@Component
public interface ChangeFlowNodeMapper {

    /**
     * Convert a list of ChangeFlowNodeEntity to a list of ChangeFlowNodeModel.
     *
     * @param entities the entities to convert
     * @return the converted models
     */
    List<ChangeFlowNodeModel> toDtoList(List<ChangeFlowNodeEntity> entities);

    ChangeFlowNodeModel cloneModel(ChangeFlowNodeModel model);


    List<ChangeFlowNodeModel> toModels(List<ChangeFlowNodeEntity> entities);

    ChangeFlowNodeModel toModel(ChangeFlowNodeEntity entity);
}