package vn.com.mbbank.kanban.mbamt.server.mapper;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestEntity;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface ChangeRequestMapper {

    ChangeRequestModel toDto(ChangeRequestEntity entity);

    List<ChangeRequestModel> toModelList(List<ChangeRequestEntity> entities);
}
