package vn.com.mbbank.kanban.mbamt.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestRoleEntity;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestRoleModel;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestRoleUserModel;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ChangeRequestRoleUserMapper.class})
@Component
public interface ChangeRequestRoleMapper {

    @Mapping(target = "users", source = "users")
    ChangeRequestRoleModel toDto(ChangeRequestRoleEntity entity,
                                 List<ChangeRequestRoleUserModel> users);

    List<ChangeRequestRoleModel> mapToDtos(List<ChangeRequestRoleEntity> entitys);

    ChangeRequestRoleEntity toEntity(ChangeRequestRoleModel roleModel);

    List<ChangeRequestRoleModel> mapTo(List<ChangeRequestRoleEntity> list);
}