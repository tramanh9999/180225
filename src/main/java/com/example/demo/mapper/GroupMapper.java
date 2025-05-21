package com.example.demo.mapper;

import com.example.demo.dtos.GroupDto;
import com.example.demo.entity.GroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    @Mapping(source = "isChangeRole", target = "isChangeRole")
    GroupDto toDto(GroupEntity entity);

    @Mapping(source = "isChangeRole", target = "isChangeRole")
    GroupEntity toEntity(GroupDto dto);
}
