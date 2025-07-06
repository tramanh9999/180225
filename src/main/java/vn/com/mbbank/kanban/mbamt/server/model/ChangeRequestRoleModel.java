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

    private Long changeRequestId;
    //flow node
    private long changeFlowNodeId;
    private ChangeFlowNodeModel changeFlowNode;

    @Builder.Default
    private List<ChangeRequestRoleWorkflowListModel> workflows = new ArrayList<>();

    public List<ChangeRequestRoleWorkflowListModel> getWorkflows() {
        return Objects.requireNonNullElseGet(workflows, ArrayList::new);
    }
}