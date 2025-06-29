package com.example.demo.mapper;

import com.example.demo.entity.ChangeRequestRoleEntity;
import com.example.demo.model.ChangeRequestRoleModel;
import com.example.demo.model.ChangeRequestRoleUserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ChangeRequestRoleUserMapper.class})
@Component
public interface ChangeRequestRoleMapper {

    @Mapping(target = "users", source = "users")
    ChangeRequestRoleModel toDto(ChangeRequestRoleEntity entity,
                                 List<ChangeRequestRoleUserModel> users);

    List<ChangeRequestRoleModel> mapToDtos(List<ChangeRequestRoleEntity> entitys);

    ChangeRequestRoleEntity toEntity(ChangeRequestRoleModel roleModel);
}