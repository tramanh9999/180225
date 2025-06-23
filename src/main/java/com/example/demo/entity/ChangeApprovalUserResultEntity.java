package com.example.demo.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;

@Data
@Entity
@Table(name = "CHANGE_APPROVAL_USER_RESULT")
public class ChangeApprovalUserResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CHANGE_REQUEST_APPROVAL_ID", nullable = false)
    private Long changeRequestApprovalId;

    @Column(name = "APPROVED_USER", length = 100)
    private String approvedUser;

    @Column(name = "STATUS", length = 100)
    private String status;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "COMMENT", length = 1000)
    private String comment;

    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;

    @Column(name = "MODIFIED_DATE")
    private Timestamp modifiedDate;

    @Column(name = "MODIFIED_BY", length = 100)
    private String modifiedBy;
}
