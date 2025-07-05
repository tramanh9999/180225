package vn.com.mbbank.kanban.mbamt.server.mapper;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestHistoryEntity;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestHistoryModel;
import org.mapstruct.Mapper;

// Assume ChangeRequestHistoryEntity and ChangeRequestHistoryModel are defined
// Assume ChangeStage enum is defined

@Mapper(componentModel = "spring")
public interface ChangeRequestHistoryMapper {

    ChangeRequestHistoryModel toModel(ChangeRequestHistoryEntity entity);

    ChangeRequestHistoryEntity toEntity(ChangeRequestHistoryModel model);

}