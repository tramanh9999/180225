package vn.com.mbbank.kanban.mbamt.server.mapper;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestRoleUserEntity;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestRoleUserModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface ChangeRequestRoleUserMapper {
    ChangeRequestRoleUserModel toDto(ChangeRequestRoleUserEntity entity);

    List<ChangeRequestRoleUserModel> toModelList(List<ChangeRequestRoleUserEntity> entities);

    ChangeRequestRoleUserEntity toEntity(ChangeRequestRoleUserModel model);
}