package com.example.demo.mapper;

import com.example.demo.entity.GroupEntity;
import com.example.demo.model.GroupModel;
import com.example.demo.enums.GroupType;
import com.example.demo.service.dto.BusinessException;
import com.example.demo.service.dto.ErrorCodeCommon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    @Mapping(source = "isChangeRole", target = "isChangeRole")
    @Mapping(target = "groupType", expression = "java(entity.getGroupType() != null ? entity.getGroupType().getValue() : null)")
    GroupModel toDto(GroupEntity entity);

    @Mapping(source = "isChangeRole", target = "isChangeRole")
    @Mapping(target = "groupType", ignore = true)
    GroupEntity toEntity(GroupModel dto);

    /**
     * Hàm parse enum generic, dùng cho mọi enum, throw BusinessException nếu lỗi.
     */
    static <E extends Enum<E>> E parseEnum(Class<E> enumClass, String value, ErrorCodeCommon errorCode) {
        try {
            for (E constant : enumClass.getEnumConstants()) {
                if (constant.name().equalsIgnoreCase(value)) {
                    return constant;
                }
                // Nếu enum có getValue thì so sánh luôn
                try {
                    var method = enumClass.getMethod("getValue");
                    Object enumValue = method.invoke(constant);
                    if (enumValue != null && enumValue.toString().equalsIgnoreCase(value)) {
                        return constant;
                    }
                } catch (NoSuchMethodException ignore) {
                }
            }
            throw new BusinessException(errorCode, "Giá trị enum không hợp lệ: " + value);
        } catch (Exception ex) {
            throw new BusinessException(errorCode, "Lỗi chuyển đổi enum: " + value);
        }
    }

    // Hàm cũ cho GroupType, giữ lại để tương thích
    static GroupType parseGroupType(String value) {
        return parseEnum(GroupType.class, value, ErrorCodeCommon.INVALID_ENUM_VALUE);
    }
}
