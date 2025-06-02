package com.example.demo.entity.entity;

import com.example.demo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CHANGE_REQUEST_WORKFLOW")
@Data
public class ChangeRequestWorkflowEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CHANGE_ID")
    private Long changeId;

    @Column(name = "WORKFLOW_DATA")
    private String workflowData;

}