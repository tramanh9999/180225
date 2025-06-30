package com.example.demo.model;

import com.example.demo.enums.ApprovalResultStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Model representing a change request approval.
 * It includes the ID, change request ID, change request role user ID, and overall status.
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRequestApprovalModel extends BaseModel {
    private Long id;
    private Long changeRequestId;
    private Long changeRequestRoleUserId;
    private ApprovalResultStatus overallStatus;
}
