package com.example.demo.mapper;

import com.example.demo.entity.ChangeFlowNodeEntity;
import com.example.demo.model.ChangeFlowNodeModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper for converting between ChangeFlowNodeEntity and ChangeFlowNodeModel.
 */
@Mapper(componentModel = "spring")
@Component
public interface ChangeFlowNodeMapper {


    /**
     * Convert a ChangeFlowNodeEntity to a ChangeFlowNodeModel.
     *
     * @param entity the entity to convert
     * @return the converted model
     */
    ChangeFlowNodeModel toDto(ChangeFlowNodeEntity entity);

    /**
     * Convert a list of ChangeFlowNodeEntity to a list of ChangeFlowNodeModel.
     *
     * @param entities the entities to convert
     * @return the converted models
     */
    List<ChangeFlowNodeModel> toDtoList(List<ChangeFlowNodeEntity> entities);

    ChangeFlowNodeModel cloneModel(ChangeFlowNodeModel model);


}