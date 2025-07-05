package vn.com.mbbank.kanban.mbamt.server.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    // new
    @Builder.Default
    private List<ChangeRequestRoleWorkflowListModel> workflows = new ArrayList<>();

    public List<ChangeRequestRoleUserModel> getUsers() {
        return Objects.requireNonNullElseGet(users, ArrayList::new);
    }

    public List<ChangeRequestRoleWorkflowListModel> getWorkflows() {
        return Objects.requireNonNullElseGet(workflows, ArrayList::new);
    }
}