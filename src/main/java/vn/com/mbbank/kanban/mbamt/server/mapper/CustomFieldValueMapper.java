package vn.com.mbbank.kanban.mbamt.server.mapper;

import vn.com.mbbank.kanban.mbamt.server.entity.CustomFieldValueEntity;
import vn.com.mbbank.kanban.mbamt.server.model.CustomFieldValueModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")

@Component
public interface CustomFieldValueMapper {

    CustomFieldValueModel toDto(CustomFieldValueEntity entity);

    CustomFieldValueEntity toEntity(CustomFieldValueModel dto);
}
