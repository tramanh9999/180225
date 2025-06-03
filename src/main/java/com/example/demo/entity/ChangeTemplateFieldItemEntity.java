package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CHANGE_TEMPLATE_FIELD_ITEM")
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
