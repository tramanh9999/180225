package com.example.demo.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CHANGE_REQUEST_APPROVAL")
public class ChangeRequestApprovalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CHANGE_REQUEST_ID", nullable = false)
    private Long changeRequestId;

    @Column(name = "CHANGE_REQUEST_ROLE_USER_ID", nullable = false)
    private Long changeRequestRoleUserId;

    @Column(name = "OVERALL_STATUS", length = 100)
    private String overallStatus;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;

    @Column(name = "MODIFIED_DATE")
    private Timestamp modifiedDate;

    @Column(name = "MODIFIED_BY", length = 100)
    private String modifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChangeRequestId() {
        return changeRequestId;
    }

    public void setChangeRequestId(Long changeRequestId) {
        this.changeRequestId = changeRequestId;
    }

    public Long getChangeRequestRoleUserId() {
        return changeRequestRoleUserId;
    }

    public void setChangeRequestRoleUserId(Long changeRequestRoleUserId) {
        this.changeRequestRoleUserId = changeRequestRoleUserId;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
