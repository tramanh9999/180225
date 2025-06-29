package com.example.demo.mapper;

import com.example.demo.entity.ChangeRequestHistoryEntity;
import com.example.demo.model.ChangeRequestHistoryModel;
import org.mapstruct.Mapper;

// Assume ChangeRequestHistoryEntity and ChangeRequestHistoryModel are defined
// Assume ChangeStage enum is defined

@Mapper(componentModel = "spring")
public interface ChangeRequestHistoryMapper {

    ChangeRequestHistoryModel toModel(ChangeRequestHistoryEntity entity);

    ChangeRequestHistoryEntity toEntity(ChangeRequestHistoryModel model);

}