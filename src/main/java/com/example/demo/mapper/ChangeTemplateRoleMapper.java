package com.example.demo.mapper;

import com.example.demo.entity.entity.ChangeTemplateRoleEntity;
import com.example.demo.model.ChangeTemplateRoleModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ChangeTemplateRoleMapper {
    ChangeTemplateRoleModel toModel(ChangeTemplateRoleEntity entity);

    ChangeTemplateRoleEntity toEntity(ChangeTemplateRoleModel model);
}