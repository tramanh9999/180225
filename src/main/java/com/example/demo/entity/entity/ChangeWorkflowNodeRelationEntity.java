package com.example.demo.entity.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CHANGE_WORKFLOW_NODE_RELATION")
@Data
public class ChangeWorkflowNodeRelationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "WORKFLOW_ID")
    private Long workflowId;

    @Column(name = "NODE_ID")
    private Long nodeId;

    @Column(name = "NEXT_NODE_ID")
    private Long nextNodeId;

    @ManyToOne
    @JoinColumn(name = "WORKFLOW_ID", insertable = false, updatable = false)
    private ChangeWorkflowEntity workflow;

    @ManyToOne
    @JoinColumn(name = "NODE_ID", insertable = false, updatable = false)
    private ChangeNodeEntity node;

    @ManyToOne
    @JoinColumn(name = "NEXT_NODE_ID", insertable = false, updatable = false)
    private ChangeNodeEntity nextNode;
}