package com.example.demo.model;

import lombok.*;

import java.util.List;

/**
 * Model representing a change request role.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRequestRoleModel extends BaseModel {
    private Long id;
    private Long changeFlowNodeId;
    private Long changeRequestWorkflowId;
    private Long changeRequestId;
    private List<ChangeRequestRoleUserModel> users;

    private ChangeFlowNodeModel changeFlowNode;

    private List<ChangeRequestRoleUserModel> cabUserGroups;

    private List<List<ChangeRequestRoleUserModel>> groupedCabUserGroups;
}