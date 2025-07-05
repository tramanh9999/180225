package vn.com.mbbank.kanban.mbamt.server.mapper;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeTemplateRoleEntity;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeTemplateRoleModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ChangeTemplateRoleMapper {
    ChangeTemplateRoleModel toModel(ChangeTemplateRoleEntity entity);

    ChangeTemplateRoleEntity toEntity(ChangeTemplateRoleModel model);
}