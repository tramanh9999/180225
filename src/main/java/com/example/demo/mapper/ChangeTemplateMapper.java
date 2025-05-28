package com.example.demo.mapper;

import com.example.demo.entity.ChangeTemplateEntity;
import com.example.demo.model.ChangeTemplateModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ChangeTemplateMapper {

    ChangeTemplateModel toDto(ChangeTemplateEntity entity);

    ChangeTemplateEntity toEntity(ChangeTemplateModel dto);
}
