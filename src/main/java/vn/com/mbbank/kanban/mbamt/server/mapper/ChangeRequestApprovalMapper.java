package vn.com.mbbank.kanban.mbamt.server.mapper;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestApprovalEntity;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestApprovalModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

// Make sure these classes are correctly imported from their respective packages
// import com.yourpackage.entity.ChangeRequestApprovalEntity;
// import com.yourpackage.model.ChangeRequestApprovalModel;

@Mapper(componentModel = "spring") // This makes MapStruct generate a Spring-managed component
public interface ChangeRequestApprovalMapper {

    /**
     * Converts a ChangeRequestApprovalEntity to a ChangeRequestApprovalModel (DTO).
     *
     * @param entity The ChangeRequestApprovalEntity to convert.
     * @return The corresponding ChangeRequestApprovalModel.
     */
    ChangeRequestApprovalModel toModel(ChangeRequestApprovalEntity entity);

    /**
     * Converts a ChangeRequestApprovalModel (DTO) to a ChangeRequestApprovalEntity.
     * When creating a new entity, 'id' and audit fields (createdDate, createdBy, etc.) are typically
     * ignored as they are handled by the database or JPA auditing mechanisms.
     *
     * @param model The ChangeRequestApprovalModel to convert.
     * @return The corresponding ChangeRequestApprovalEntity.
     */
    ChangeRequestApprovalEntity toEntity(ChangeRequestApprovalModel model);

    /**
     * Updates an existing ChangeRequestApprovalEntity with data from a ChangeRequestApprovalModel.
     * This is useful for update operations, allowing you to selectively copy fields from the DTO
     * to a loaded entity. ID and creation audit fields are ignored. Modification audit fields
     * might be automatically updated by JPA Auditing.
     *
     * @param model  The source ChangeRequestApprovalModel containing updated data.
     * @param entity The target ChangeRequestApprovalEntity to be updated.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true) // Often managed by auditing
    @Mapping(target = "modifiedBy", ignore = true)
    // Often managed by auditing
    void updateEntityFromModel(ChangeRequestApprovalModel model,
                               @MappingTarget ChangeRequestApprovalEntity entity);

    List<ChangeRequestApprovalEntity> toEntity(
            List<ChangeRequestApprovalModel> approvalModelsFromRoles);

    List<ChangeRequestApprovalModel> toModel(List<ChangeRequestApprovalEntity> savedApproval);
}