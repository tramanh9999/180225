package com.example.demo.mapper;

import com.example.demo.dtos.ChangeTemplateModel;
import com.example.demo.entity.ChangeTemplateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChangeTemplateMapper {

    ChangeTemplateModel toDto(ChangeTemplateEntity entity);

    ChangeTemplateEntity toEntity(ChangeTemplateModel dto);
}
