package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CHANGE_REQUEST_ROLE")
@Data
public class ChangeRequestRoleEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CHANGE_FLOW_NODE_ID")
    private Long changeFlowNodeId;

    @Column(name = "CHANGE_REQUEST_WORKFLOW_ID")
    private Long changeRequestWorkflowId;

    @Column(name = "CAB_GROUP")
    private Integer cabGroup;

    @Column(name = "CHANGE_REQUEST_ID")
    private Long changeRequestId;

}