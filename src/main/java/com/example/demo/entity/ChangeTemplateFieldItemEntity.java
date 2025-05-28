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
@Table(name = "change_template_field_item")
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ChangeTemplateFieldItemEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "CHANGE_TEMPLATE_ID")
    private Long changeTemplateId;

    @Basic
    @Column(name = "CUSTOM_FIELD_ID")
    private Long customFieldId;

    @Basic
    @Column(name = "FIELD_VALUE")
    private String fieldValue;
}
