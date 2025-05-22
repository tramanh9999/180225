package com.example.demo.mapper;

import com.example.demo.entity.ChangeTemplateRoleEntity;
import com.example.demo.model.ChangeTemplateRoleModel;

import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ChangeTemplateRoleMapper {
    ChangeTemplateRoleModel toModel(ChangeTemplateRoleEntity entity);

    ChangeTemplateRoleEntity toEntity(ChangeTemplateRoleModel model);
}