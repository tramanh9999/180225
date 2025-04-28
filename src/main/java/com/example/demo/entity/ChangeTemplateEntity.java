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
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "change_template")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ChangeTemplateEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "CHANGE_TEMPLATE_SEQ", sequenceName = "CHANGE_TEMPLATE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHANGE_TEMPLATE_SEQ")
    private Long id;

    @Basic
    @Column(name = "NAME")
    private String name;

    @Basic
    @Column(name = "DESCRIPTION")
    private String description;
}
