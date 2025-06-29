package com.example.demo.model;

import com.example.demo.enums.ApprovalResultStatus;
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