package com.example.demo.mapper;

import com.example.demo.entity.entity.ChangeRequestEntity;
import com.example.demo.model.ChangeRequestModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface ChangeRequestMapper {

    ChangeRequestModel toDto(ChangeRequestEntity entity);

    List<ChangeRequestModel> toModelList(List<ChangeRequestEntity> entities);
}
