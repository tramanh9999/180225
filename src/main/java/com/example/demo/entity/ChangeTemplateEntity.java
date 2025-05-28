package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CHANGE_TEMPLATE")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ChangeTemplateEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "NOTICE", length = 255) // Assuming VARCHAR2 default length or specify
    private String notice;

    @Basic
    @Column(name = "IS_ACTIVE")
    private Integer isActive; // Assuming NUMBER maps to Integer, 1 for true, 0 for false

    @Basic
    @Column(name = "DELETED")
    private Integer deleted; // 0 for not deleted, 1 for deleted
}
