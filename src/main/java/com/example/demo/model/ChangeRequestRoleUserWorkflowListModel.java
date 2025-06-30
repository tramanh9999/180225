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
public class ChangeRequestRoleUserWorkflowListModel extends BaseModel {

    Long changeWorkflowId;
    String changeWorkflowName;
    List<ChangeRequestRoleUserListModel> groups;
}