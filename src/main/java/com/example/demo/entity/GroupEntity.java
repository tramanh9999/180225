package com.example.demo.entity;

import com.example.demo.enums.GroupType;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYS_GROUP") // "GROUP" is a reserved keyword in SQL, so table name might need
// adjustment
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
