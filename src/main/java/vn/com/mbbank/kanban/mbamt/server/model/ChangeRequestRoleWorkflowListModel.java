package vn.com.mbbank.kanban.mbamt.server.model;

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
public class ChangeRequestRoleWorkflowListModel extends BaseModel {

    Long changeWorkflowId;
    String changeWorkflowName;
    List<ChangeRequestRoleUserListModel> groups;
}