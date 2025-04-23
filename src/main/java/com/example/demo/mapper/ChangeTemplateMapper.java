package com.example.demo.mapper;

import com.example.demo.dtos.ChangeTemplateDto;
import com.example.demo.entity.ChangeTemplateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChangeTemplateMapper {

    ChangeTemplateDto toDto(ChangeTemplateEntity entity);

    ChangeTemplateEntity toEntity(ChangeTemplateDto dto);
}
