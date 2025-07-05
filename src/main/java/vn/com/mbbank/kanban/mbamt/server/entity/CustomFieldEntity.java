package vn.com.mbbank.kanban.mbamt.server.entity;

import vn.com.mbbank.kanban.mbamt.server.enums.CustomFieldType;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "custom_field")
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomFieldEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "NAME")
    private String name;

    @Basic
    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private CustomFieldType type;

    @Basic
    @Column(name = "IS_MULTIPLE")
    private Boolean isMultiple;

}
