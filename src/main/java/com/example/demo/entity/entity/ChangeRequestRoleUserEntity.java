package com.example.demo.entity.entity;

import com.example.demo.entity.BaseEntity;
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
    private Integer cabGroupOrder;

}