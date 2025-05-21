package com.example.demo.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CHANGE_TEMPLATE_ROLE")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeTemplateRoleEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "CHANGE_TEMPLATE_ROLE_SEQ", sequenceName = "CHANGE_TEMPLATE_ROLE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHANGE_TEMPLATE_ROLE_SEQ")
    private Long id;

    @Basic
    @Column(name = "change_template_id")
    private Long changeTemplateId;

    @Basic
    @Column(name = "group_id")
    private Long groupId;

    @Basic
    @Column(name = "level") // "LEVEL" can be a reserved keyword in some SQL dialects
    private Integer level;

    @Basic
    @Column(name = "order") // "ORDER" is a reserved keyword in SQL
    private Integer roleOrder;
}
