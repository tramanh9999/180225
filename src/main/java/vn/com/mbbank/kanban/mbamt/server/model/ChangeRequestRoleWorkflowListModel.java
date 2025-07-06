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
public class ChangeRequestRoleWorkflowListModel extends BaseModel {

    Long changeWorkflowId;
    String changeWorkflowName;


    @Builder.Default
    List<ChangeRequestRoleUserListModel> groups = new ArrayList<>();

    public List<ChangeRequestRoleUserListModel> getGroups() {
        return Objects.requireNonNullElseGet(groups, ArrayList::new);
    }

}