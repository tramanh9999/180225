package com.example.demo.mapper;

import com.example.demo.entity.ChangeRequestApprovalResultEntity;
import com.example.demo.model.ChangeRequestApprovalResultModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

// Assuming ChangeApprovalUserResultEntity and ChangeApprovalUserResultModel are defined
// (as per your previous requests)
// import com.yourpackage.entity.ChangeApprovalUserResultEntity;
// import com.yourpackage.model.ChangeApprovalUserResultModel;

@Mapper(componentModel = "spring") // componentModel="spring" makes it a Spring bean
public interface ChangeApprovalUserResultMapper {

    /**
     * Converts a ChangeApprovalUserResultEntity to a ChangeApprovalUserResultModel.
     *
     * @param entity The entity to convert.
     * @return The converted model.
     */
    ChangeRequestApprovalResultModel toModel(ChangeRequestApprovalResultEntity entity);

    /**
     * Converts a ChangeApprovalUserResultModel to a ChangeApprovalUserResultEntity.
     * When creating a new entity, 'id' and audit fields (createdDate, createdBy, modifiedDate, modifiedBy)
     * are often ignored as they are handled by the database or JPA auditing.
     *
     * @param model The model to convert.
     * @return The converted entity.
     */

    ChangeRequestApprovalResultEntity toEntity(ChangeRequestApprovalResultModel model);

    /**
     * Updates an existing ChangeApprovalUserResultEntity from a ChangeApprovalUserResultModel.
     * This is useful for update operations to selectively copy fields.
     * ID and creation audit fields are ignored as they should not change.
     * Modification audit fields might be automatically handled by JPA Auditing.
     *
     * @param model  The source model with updated data.
     * @param entity The target entity to update.
     */

    // Often managed by auditing
    void updateEntityFromModel(ChangeRequestApprovalResultModel model,
                               @MappingTarget ChangeRequestApprovalResultEntity entity);
}