package com.example.demo.entity.entity;

import com.example.demo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CHANGE_REQUEST_WORKFLOW_DETAIL")
@Data
public class ChangeRequestWorkflowDetailEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CHANGE_REQUEST_WORKFLOW_ID")
    private Long changeRequestWorkflowId;

    @Column(name = "CHANGE_NODE_ID")
    private Long changeNodeId;

    
}