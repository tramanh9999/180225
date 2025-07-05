package vn.com.mbbank.kanban.mbamt.server.mapper;

import vn.com.mbbank.kanban.mbamt.server.entity.SysGroupEntity;
import vn.com.mbbank.kanban.mbamt.server.enums.GroupType;
import vn.com.mbbank.kanban.mbamt.server.model.BusinessException;
import vn.com.mbbank.kanban.mbamt.server.model.ErrorCodeCommon;
import vn.com.mbbank.kanban.mbamt.server.model.SysGroupModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    /**
     * Hàm parse enum generic, dùng cho mọi enum, throw BusinessException nếu lỗi.
     */
    static <E extends Enum<E>> E parseEnum(Class<E> enumClass, String value,
                                           ErrorCodeCommon errorCode) {
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

    SysGroupModel toDto(SysGroupEntity entity);

    SysGroupEntity toEntity(SysGroupModel dto);
}
