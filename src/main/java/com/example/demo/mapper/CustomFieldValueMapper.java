package com.example.demo.mapper;

import com.example.demo.dtos.CustomFieldValueDto;
import com.example.demo.entity.CustomFieldValueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomFieldValueMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "customFieldId", source = "customFieldId")
    @Mapping(target = "fieldValue", source = "fieldValue")
    @Mapping(target = "min", source = "min")
    @Mapping(target = "max", source = "max")
    @Mapping(target = "isDefault", source = "isDefault")
    CustomFieldValueDto toDto(CustomFieldValueEntity entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "customFieldId", source = "customFieldId")
    @Mapping(target = "fieldValue", source = "fieldValue")
    @Mapping(target = "min", source = "min")
    @Mapping(target = "max", source = "max")
    @Mapping(target = "isDefault", source = "isDefault")
    CustomFieldValueEntity toEntity(CustomFieldValueDto dto);
}
