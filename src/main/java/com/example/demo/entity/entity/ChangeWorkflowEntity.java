package com.example.demo.entity.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "CHANGE_WORKFLOW")
@Data
public class ChangeWorkflowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "workflowId")
    private List<ChangeRequestWorkflowEntity> changeRequestWorkflows;
}