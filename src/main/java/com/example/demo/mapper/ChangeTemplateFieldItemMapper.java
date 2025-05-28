package com.example.demo.mapper;

import com.example.demo.entity.ChangeTemplateFieldItemEntity;
import com.example.demo.model.ChangeTemplateFieldItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ChangeTemplateFieldItemMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "changeTemplateId", source = "changeTemplateId")
    @Mapping(target = "customFieldId", source = "customFieldId")
    @Mapping(target = "fieldValue", source = "fieldValue")
    ChangeTemplateFieldItemDto toDto(ChangeTemplateFieldItemEntity entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "changeTemplateId", source = "changeTemplateId")
    @Mapping(target = "customFieldId", source = "customFieldId")
    @Mapping(target = "fieldValue", source = "fieldValue")
    ChangeTemplateFieldItemEntity toEntity(ChangeTemplateFieldItemDto dto);
}
