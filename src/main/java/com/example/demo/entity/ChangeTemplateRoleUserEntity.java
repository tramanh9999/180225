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
@Table(name = "CHANGE_TEMPLATE_ROLE_USER")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeTemplateRoleUserEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "CHANGE_TEMPLATE_ROLE_USER_SEQ", sequenceName = "CHANGE_TEMPLATE_ROLE_USER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHANGE_TEMPLATE_ROLE_USER_SEQ")
    private Long id;

    @Basic
    @Column(name = "change_template_role_id")
    private Long changeTemplateRoleId;

    @Basic
    @Column(name = "username") // Assuming VARCHAR, length might need to be specified
    private String username;
}
