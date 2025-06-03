package com.example.demo.mapper;

import com.example.demo.entity.CustomFieldValueEntity;
import com.example.demo.model.CustomFieldValueDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")

@Component
public interface CustomFieldValueMapper {

    CustomFieldValueDto toDto(CustomFieldValueEntity entity);

    CustomFieldValueEntity toEntity(CustomFieldValueDto dto);
}
