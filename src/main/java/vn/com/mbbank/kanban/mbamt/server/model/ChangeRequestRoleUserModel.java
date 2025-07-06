package vn.com.mbbank.kanban.mbamt.server.model;

import lombok.*;

/**
 * Model representing a change request role user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChangeRequestRoleUserModel extends BaseModel {
    ChangeRequestApprovalModel approvalModel;
    private Long id;
    private Long changeRequestRoleId;
    private String username;
    private String cabGroupName;
    private int cabGroup;
    private int cabGroupOrder;
    //changeRequestWorkflowDetailId== null -> custom approval user, else workflow node approvaluser
    private Long changeRequestWorkflowDetailId;
    //don't delete this field, it is for custom approvaluser
    private long changeRequestWorkflowId;
    private String changeRequestWorkflowName;
    private Long changeNodeId;
    private String changeNodeName;
}
