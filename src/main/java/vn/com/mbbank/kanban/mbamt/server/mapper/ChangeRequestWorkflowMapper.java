package vn.com.mbbank.kanban.mbamt.server.mapper;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestWorkflowEntity;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestWorkflowModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper for converting between ChangeRequestWorkflowEntity and ChangeRequestWorkflowModel.
 */
@Mapper(componentModel = "spring")
@Component
public interface ChangeRequestWorkflowMapper {

    /**
     * Convert a ChangeRequestWorkflowEntity to a ChangeRequestWorkflowModel.
     *
     * @param entity the entity to convert
     * @return the converted model
     */
    ChangeRequestWorkflowModel toDto(ChangeRequestWorkflowEntity entity);

    /**
     * Convert a list of ChangeRequestWorkflowEntity to a list of ChangeRequestWorkflowModel.
     *
     * @param entities the entities to convert
     * @return the converted models
     */
    List<ChangeRequestWorkflowModel> toDtoList(List<ChangeRequestWorkflowEntity> entities);
}