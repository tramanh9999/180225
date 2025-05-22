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
import com.example.demo.enums.GroupType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "GROUP") // "GROUP" is a reserved keyword in SQL, so table name might need adjustment
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "GROUP_SEQ", sequenceName = "GROUP_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GROUP_SEQ")
    private Long id;

    @Basic
    @Column(name = "name", length = 255) // Assuming VARCHAR2 default length or specify
    private String name;

    @Basic
    @Column(name = "description", length = 255) // Assuming VARCHAR2 default length or specify
    private String description;

    @Basic
    @Column(name = "deleted")
    private Integer deleted; // 0 for not deleted, 1 for deleted

    @Basic
    @Column(name = "is_change_role")
    private Boolean isChangeRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "group_type", length = 50)
    private GroupType groupType;
}
