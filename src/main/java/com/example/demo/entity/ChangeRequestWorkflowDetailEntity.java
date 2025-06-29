package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "CHANGE_REQUEST_WORKFLOW_DETAIL")
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ChangeRequestWorkflowDetailEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CHANGE_REQUEST_WORKFLOW_ID")
    private Long changeRequestWorkflowId;

    @Column(name = "CHANGE_NODE_ID")
    private Long changeNodeId;


}