package com.example.demo.mapper;

import com.example.demo.entity.ChangeTemplateFieldItemEntity;
import com.example.demo.model.ChangeTemplateFieldItemDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ChangeTemplateFieldItemMapper {


    ChangeTemplateFieldItemDto toDto(ChangeTemplateFieldItemEntity entity);

    ChangeTemplateFieldItemEntity toEntity(ChangeTemplateFieldItemDto dto);
}
