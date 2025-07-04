package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CHANGE_REQUEST_ROLE_USER")
@Data
public class ChangeRequestRoleUserEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CHANGE_REQUEST_ROLE_ID")
    private Long changeRequestRoleId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "CAB_GROUP_ORDER")
    private int cabGroupOrder;
    
    @Column(name = "CAB_GROUP")
    private int cabGroup;

    @Column(name = "CHANGE_REQUEST_ID")
    private Long changeRequestId;


    @Column(name = "CHANGE_REQUEST_WORKFLOW_DETAIL_ID")
    private Long changeRequestWorkflowDetailId;
}