package com.example.demo.model;

import com.example.demo.enums.ApprovalResultStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeRequestApprovalResultModel extends BaseModel {
    private Long id;
    private Long changeRequestApprovalId;
    private String approvedUser;
    private ApprovalResultStatus status;
    private String comment;
}