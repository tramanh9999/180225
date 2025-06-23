package com.example.demo.mapper;

import com.example.demo.entity.ChangeRequestRoleUserEntity;
import com.example.demo.model.ChangeRequestRoleUserModel;
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