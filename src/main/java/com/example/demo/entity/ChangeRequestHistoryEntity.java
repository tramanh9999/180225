package com.example.demo.entity;

import com.example.demo.enums.ApprovalResultStatus;
import com.example.demo.enums.ChangeStage;
import jakarta.persistence.*;
import lombok.*;

// Assuming BaseEntity<ID> exists and provides common audit fields (createdDate, createdBy, modifiedDate, modifiedBy)
// public abstract class BaseEntity<ID> {
//     @Column(name = "CREATED_DATE", updatable = false)
//     protected LocalDateTime createdDate;
//     @Column(name = "CREATED_BY", updatable = false)
//     protected String createdBy;
//     @Column(name = "MODIFIED_DATE")
//     protected LocalDateTime modifiedDate;
//     @Column(name = "MODIFIED_BY")
//     protected String modifiedBy;
//     // Getters, Setters omitted for brevity
// }

@Entity
@Table(name = "CHANGE_REQUEST_HISTORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Ensures builder inheritance if BaseEntity uses it
public class ChangeRequestHistoryEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CHANGE_REQUEST_ID", nullable = false)
    private Long changeRequestId;


    @Column(name = "OLD_CHANGE_STATUS_ID")
    private Long oldChangeStatusId;

    @Column(name = "NEW_CHANGE_STATUS_ID")
    private Long newChangeStatusId;

    @Column(name = "OLD_CHANGE_FLOW_NODE_ID", length = 255)
    private Long oldChangeFlowNodeId;

    @Column(name = "NEW_CHANGE_FLOW_NODE_ID", length = 255)
    private Long newChangeFlowNodeId;


    @Column(name = "ACTION_TAKEN", length = 100)
    @Enumerated(EnumType.STRING)
    private ApprovalResultStatus actionTaken;


    @Column(name = "NEW_CHANGE_STAGE", length = 255)
    @Enumerated(EnumType.STRING)
    private ChangeStage newChangeStage;


    @Column(name = "OLD_CHANGE_STAGE", length = 255)
    @Enumerated(EnumType.STRING)
    private ChangeStage oldChangeStage;

}