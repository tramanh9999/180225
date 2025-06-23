package com.example.demo.mapper;

import com.example.demo.entity.ChangeTemplateFieldItemEntity;
import com.example.demo.model.ChangeTemplateFieldItemModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ChangeTemplateFieldItemMapper {


    ChangeTemplateFieldItemModel toDto(ChangeTemplateFieldItemEntity entity);

    ChangeTemplateFieldItemEntity toEntity(ChangeTemplateFieldItemModel dto);
}
