package vn.com.mbbank.kanban.mbamt.server.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "CHANGE_FLOW_NODE_GROUP")
public class ChangeFlowNodeGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "change_flow_node_group_seq")
    @SequenceGenerator(name = "change_flow_node_group_seq",
            sequenceName = "CHANGE_FLOW_NODE_GROUP_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "CHANGE_FLOW_NODE_ID")
    private Long changeFlowNodeId;

    @Column(name = "GROUP_ID")
    private Long groupId;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "CREATED_BY", length = 255)
    private String createdBy;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;

    @Column(name = "MODIFIED_BY", length = 255)
    private String modifiedBy;

    // Default constructor
    public ChangeFlowNodeGroupEntity() {
    }

    // Constructor with fields
    public ChangeFlowNodeGroupEntity(Long id, Long changeFlowNodeId, Long groupId,
                                     LocalDateTime createdDate, String createdBy,
                                     LocalDateTime modifiedDate, String modifiedBy) {
        this.id = id;
        this.changeFlowNodeId = changeFlowNodeId;
        this.groupId = groupId;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.modifiedDate = modifiedDate;
        this.modifiedBy = modifiedBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChangeFlowNodeId() {
        return changeFlowNodeId;
    }

    public void setChangeFlowNodeId(Long changeFlowNodeId) {
        this.changeFlowNodeId = changeFlowNodeId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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

    @Override
    public String toString() {
        return "ChangeFlowNodeGroup{" + "id=" + id + ", changeFlowNodeId=" + changeFlowNodeId +
                ", groupId=" + groupId + ", createdDate=" + createdDate + ", createdBy='" +
                createdBy + '\'' + ", modifiedDate=" + modifiedDate + ", modifiedBy='" +
                modifiedBy + '\'' + '}';
    }
}
