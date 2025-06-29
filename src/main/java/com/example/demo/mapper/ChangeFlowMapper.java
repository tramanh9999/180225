package com.example.demo.mapper;

import com.example.demo.entity.ChangeFlowEntity;
import com.example.demo.model.ChangeFlowModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // componentModel="spring" makes it a Spring bean
public interface ChangeFlowMapper {
    ChangeFlowModel toModel(ChangeFlowEntity entity);

    ChangeFlowEntity toEntity(ChangeFlowModel model);

}