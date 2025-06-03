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
public class ChangeRequestRoleModel extends BaseDto<Long> {

    private Long id;

    private Long changeFlowNodeId;

    private Long changeRequestWorkflowId;

    private Integer cabGroup;

    private Long changeRequestId;

    private List<ChangeRequestRoleUserModel> users;
}