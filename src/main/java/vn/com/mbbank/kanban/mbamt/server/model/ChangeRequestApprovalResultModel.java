package vn.com.mbbank.kanban.mbamt.server.model;

import vn.com.mbbank.kanban.mbamt.server.enums.ApprovalResultStatus;
import lombok.*;

/**
 * Model representing the result of a change request approval.
 * It includes the ID, change request approval ID, approved user, status, and comment.
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeRequestApprovalResultModel extends BaseModel {
    private Long id;
    private Long changeRequestApprovalId;
    private String approvedUser;
    private ApprovalResultStatus status;
    private String replyComment;
}