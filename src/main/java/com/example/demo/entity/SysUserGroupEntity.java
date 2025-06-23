package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "SYS_USER_GROUP")
@Data
public class SysUserGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "GROUP_ID")
    private Long groupId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private SysUserEntity user;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID", insertable = false, updatable = false)
    private SysGroupEntity group;
}