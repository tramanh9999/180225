package vn.com.mbbank.kanban.mbamt.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestRoleEntity;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestRoleModel;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ChangeRequestRoleUserMapper.class})
@Component
public interface ChangeRequestRoleMapper {


    List<ChangeRequestRoleModel> mapToDtos(List<ChangeRequestRoleEntity> entitys);

    ChangeRequestRoleEntity toEntity(ChangeRequestRoleModel roleModel);

    List<ChangeRequestRoleModel> mapTo(List<ChangeRequestRoleEntity> list);
}