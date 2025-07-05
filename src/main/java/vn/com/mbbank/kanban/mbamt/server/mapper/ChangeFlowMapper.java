package vn.com.mbbank.kanban.mbamt.server.mapper;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeFlowEntity;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeFlowModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // componentModel="spring" makes it a Spring bean
public interface ChangeFlowMapper {
    ChangeFlowModel toModel(ChangeFlowEntity entity);

    ChangeFlowEntity toEntity(ChangeFlowModel model);

}