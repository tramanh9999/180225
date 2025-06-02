package com.example.demo.entity.entity;

import com.example.demo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CHANGE_FLOW_NODE")
@Data
public class ChangeFlowNodeEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CHANGE_FLOW_ID")
    private Long changeFlowId;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "LEVEL")
    private Integer level;
}