package com.example.demo.entity.entity;

import com.example.demo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CHANGE_FLOW_NODE_GROUP")
@Data
public class ChangeFlowNodeGroupEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "GROUP_ID")
    private Long groupId;
}