package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CHANGE_TEMPLATE_ROLE")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeTemplateRoleEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "CHANGE_TEMPLATE_ID")
    private Long changeTemplateId;

    @Basic
    @Column(name = "GROUP_ID")
    private Long groupId;

    @Basic
    @Column(name = "LEVEL") // "LEVEL" can be a reserved keyword in some SQL dialects
    private Integer level;

    @Basic
    @Column(name = "ORDER") // "ORDER" is a reserved keyword in SQL
    private Integer roleOrder;
}
