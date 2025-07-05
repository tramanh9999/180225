package vn.com.mbbank.kanban.mbamt.server.mapper;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeTemplateFieldItemEntity;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeTemplateFieldItemModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ChangeTemplateFieldItemMapper {


    ChangeTemplateFieldItemModel toDto(ChangeTemplateFieldItemEntity entity);

    ChangeTemplateFieldItemEntity toEntity(ChangeTemplateFieldItemModel dto);
}
