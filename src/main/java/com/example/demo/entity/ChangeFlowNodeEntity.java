package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "CHANGE_FLOW_NODE")
public class ChangeFlowNodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "change_flow_node_seq")
    @SequenceGenerator(name = "change_flow_node_seq", sequenceName = "CHANGE_FLOW_NODE_SEQ",
            allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "CHANGE_FLOW_ID")
    private Long changeFlowId;

    @Column(name = "TYPE", length = 255)
    private String type;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "CREATED_BY", length = 255)
    private String createdBy;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;

    @Column(name = "MODIFIED_BY", length = 255)
    private String modifiedBy;

    @Column(name = "NAME", length = 255)
    private String name;

    @Column(name = "NODE_LEVEL")
    private Integer nodeLevel;

    @Column(name = "NODE_ID", length = 255)
    private String nodeId;

    // Default constructor
    public ChangeFlowNodeEntity() {
    }

    // Constructor with fields
    public ChangeFlowNodeEntity(Long id, Long changeFlowId, String type, LocalDateTime createdDate,
                                String createdBy, LocalDateTime modifiedDate, String modifiedBy,
                                String name, Integer nodeLevel, String nodeId) {
        this.id = id;
        this.changeFlowId = changeFlowId;
        this.type = type;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.modifiedDate = modifiedDate;
        this.modifiedBy = modifiedBy;
        this.name = name;
        this.nodeLevel = nodeLevel;
        this.nodeId = nodeId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChangeFlowId() {
        return changeFlowId;
    }

    public void setChangeFlowId(Long changeFlowId) {
        this.changeFlowId = changeFlowId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(Integer nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String toString() {
        return "ChangeFlowNode{" + "id=" + id + ", changeFlowId=" + changeFlowId + ", type='" +
                type + '\'' + ", name='" + name + '\'' + ", nodeLevel=" + nodeLevel + ", nodeId='" +
                nodeId + '\'' + '}';
    }
}
