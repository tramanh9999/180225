package com.example.demo.model;

import com.example.demo.enums.ApprovalResultStatus;
import lombok.*;

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
    private Long oldChangeFlowNodeId;
    private Long newChangeFlowNodeId;
    private ApprovalResultStatus actionTaken;
}