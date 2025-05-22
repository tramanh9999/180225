package com.example.demo.mapper;

import com.example.demo.entity.GroupEntity;
import com.example.demo.model.GroupModel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    @Mapping(source = "isChangeRole", target = "isChangeRole")
    @Mapping(target = "groupType", expression = "java(entity.getGroupType() != null ? entity.getGroupType().getValue() : null)")
    GroupModel toDto(GroupEntity entity);

    @Mapping(source = "isChangeRole", target = "isChangeRole")
    @Mapping(target = "groupType", ignore = true)
    GroupEntity toEntity(GroupModel dto);
}
