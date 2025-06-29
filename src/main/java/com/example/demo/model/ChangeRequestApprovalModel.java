package com.example.demo.model;

import com.example.demo.enums.ApprovalResultStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRequestApprovalModel extends BaseModel {
    private Long id;
    private Long changeRequestId;
    private Long changeRequestRoleUserId;
    private ApprovalResultStatus overallStatus;
}
