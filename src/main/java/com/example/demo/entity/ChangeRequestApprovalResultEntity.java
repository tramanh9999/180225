package com.example.demo.entity;

import com.example.demo.enums.ApprovalResultStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Change request approval result entity.
 */
@Data
@Entity
@Table(name = "CHANGE_REQUEST_APPROVAL_RESULT")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRequestApprovalResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CHANGE_REQUEST_APPROVAL_ID", nullable = false)
    private Long changeRequestApprovalId;

    @Column(name = "APPROVED_USER", length = 100)
    private String approvedUser;

    @Column(name = "STATUS", length = 100)
    @Enumerated(EnumType.STRING)
    private ApprovalResultStatus status;

    @Column(name = "REPLY_COMMENT", length = 1000)
    private String replyComment;

}
