package com.example.demo.mapper;

import com.example.demo.entity.ChangeNodeEntity;
import com.example.demo.model.ChangeNodeModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper for converting between ChangeNodeEntity and ChangeNodeModel.
 */
@Mapper(componentModel = "spring")
@Component
public interface ChangeNodeMapper {

    /**
     * Convert a list of ChangeNodeEntity to a list of ChangeNodeModel.
     *
     * @param entities the entities to convert
     * @return the converted models
     */
    List<ChangeNodeModel> toDtoList(List<ChangeNodeEntity> entities);

    ChangeNodeModel cloneModel(ChangeNodeModel model);


    List<ChangeNodeModel> toModels(List<ChangeNodeEntity> entities);

    ChangeNodeModel toModel(ChangeNodeEntity entity);
}