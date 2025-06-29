package com.example.demo.mapper;

import com.example.demo.entity.ChangeStatusEntity;
import com.example.demo.model.ChangeStatusModel;
import org.mapstruct.Mapper;

// @Mapper annotation là bắt buộc để MapStruct nhận diện
// componentModel = "spring" cho phép MapStruct tạo ra một bean Spring
// (Autowired được trong các service khác)
@Mapper(componentModel = "spring")
public interface ChangeStatusMapper {

    // Nếu không có MapStruct, bạn sẽ phải viết triển khai thủ công cho các phương thức này.
    // Với MapStruct, bạn chỉ cần định nghĩa interface.

    // Chuyển đổi từ Entity sang Model (DTO)
    ChangeStatusModel toModel(ChangeStatusEntity entity);

    // Chuyển đổi từ Model (DTO) sang Entity
    // @Mapping(target = "id", ignore = true) // Thường bỏ qua ID khi tạo Entity mới (DB tự sinh)
    // @Mapping(target = "createdDate", ignore = true) // Bỏ qua audit fields khi tạo mới
    // @Mapping(target = "createdBy", ignore = true)
    // @Mapping(target = "modifiedDate", ignore = true)
    // @Mapping(target = "modifiedBy", ignore = true)
    ChangeStatusEntity toEntity(ChangeStatusModel model);
}