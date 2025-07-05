package vn.com.mbbank.kanban.mbamt.server.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import vn.com.mbbank.kanban.mbamt.server.enums.ApprovalResultStatus;

/**
 * Model representing the result of a change request approval.
 * It includes the ID, change request approval ID, approved user, status, and comment.
 */

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@RequiredArgsConstructor
public class ChangeRequestApprovalResultModel extends BaseModel {
    private Long id;
    private Long changeRequestApprovalId;
    private String approvedUser;
    private ApprovalResultStatus status;
    private String replyComment;
}