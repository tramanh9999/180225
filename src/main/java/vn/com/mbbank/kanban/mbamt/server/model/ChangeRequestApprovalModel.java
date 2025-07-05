package vn.com.mbbank.kanban.mbamt.server.model;

import vn.com.mbbank.kanban.mbamt.server.enums.ApprovalResultStatus;
import lombok.*;

/**
 * Model representing a change request approval.
 * It includes the ID, change request ID, change request role user ID, and overall status.
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeRequestApprovalModel extends BaseModel {
    private Long id;
    private Long changeRequestId;
    private Long changeRequestRoleUserId;
    private ApprovalResultStatus overallStatus;
    private String overallUsername;
}
