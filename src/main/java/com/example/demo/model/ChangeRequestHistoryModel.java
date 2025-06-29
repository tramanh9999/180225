package com.example.demo.model;

import com.example.demo.enums.ApprovalResultStatus;
import com.example.demo.enums.ChangeStage;
import lombok.*;

import java.time.LocalDateTime;

// Assuming ChangeStage enum is defined somewhere (e.g., within ChangeStatusEntity or as a top-level enum)
// public enum ChangeStage { PLANNING, APPROVAL, IMPLEMENTATION, REVIEW_CLOSE, ... }

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeRequestHistoryModel {
    private Long id;
    private Long changeRequestId;
    private Long oldChangeStatusId;
    private Long newChangeStatusId;
    private String oldChangeStatusName;
    private String newChangeStatusName;
    private String oldChangeFlowNodeHandleId;
    private String newChangeFlowNodeHandleId;
    private ApprovalResultStatus actionTaken;
    private ChangeStage newChangeStage; // New field
    private ChangeStage oldChangeStage; // New field
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime modifiedDate;
    private String modifiedBy;
}