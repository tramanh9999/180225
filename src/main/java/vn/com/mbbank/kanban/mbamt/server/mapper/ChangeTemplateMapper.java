package vn.com.mbbank.kanban.mbamt.server.mapper;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeTemplateEntity;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeTemplateModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ChangeTemplateMapper {

    ChangeTemplateModel toDto(ChangeTemplateEntity entity);

    ChangeTemplateEntity toEntity(ChangeTemplateModel dto);
}
