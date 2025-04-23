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
@Table(name = "custom_field_value")
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomFieldValueEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "CUSTOM_FIELD_VALUE_SEQ", sequenceName = "CUSTOM_FIELD_VALUE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOM_FIELD_VALUE_SEQ")
    private Long id;

    @Basic
    @Column(name = "CUSTOM_FIELD_ID")
    private Long customFieldId;

    @Basic
    @Column(name = "VALUE")
    private String value;

    @Basic
    @Column(name = "MIN")
    private Long min;

    @Basic
    @Column(name = "MAX")
    private Long max;

    @Basic
    @Column(name = "IS_DEFAULT")
    private Boolean isDefault;
}
