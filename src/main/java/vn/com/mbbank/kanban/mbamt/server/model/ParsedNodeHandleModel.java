package vn.com.mbbank.kanban.mbamt.server.model;

import vn.com.mbbank.kanban.mbamt.server.enums.ApprovalResultStatus;
import vn.com.mbbank.kanban.mbamt.server.enums.HandleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing a parsed handle in a workflow system.
 * It includes the raw handle ID, type, change status ID, and custom action.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedNodeHandleModel {
    private String rawHandleId;

    private String nodeId;
    private HandleType type;
    private Long changeStatusId;

    private ApprovalResultStatus approvedAction = ApprovalResultStatus.UNKNOWN;
}
