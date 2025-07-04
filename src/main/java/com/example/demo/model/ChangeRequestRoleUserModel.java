package com.example.demo.model;

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
    private int cabGroup;
    private int cabGroupOrder;
    private Long changeRequestWorkflowDetailId;
    private Long changeRequestWorkflowId;
    private String changeRequestWorkflowName;
    private Long changeNodeId;
    private String changeNodeName;

}
