package vn.com.mbbank.kanban.mbamt.server.model;

import vn.com.mbbank.kanban.mbamt.server.enums.ApprovalResultStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalReplyModel {
    private ApprovalResultStatus status; // APPROVED, REJECTED, CANCELLED
    private String comment;
    // Potentially other fields like delegation info
}