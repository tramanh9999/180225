package vn.com.mbbank.kanban.mbamt.server.model;


import lombok.*;

/**
 * Model representing a change request.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRequestModel extends BaseModel {

    private Long id;
    private String title;
    private String description;
    private String status;
    private Integer deleted;
    private Long changeTemplateId;
    private Long changeStatusId;
    private String changeFlowNodeStrId;
}
