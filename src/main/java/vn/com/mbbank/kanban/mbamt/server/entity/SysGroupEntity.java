package vn.com.mbbank.kanban.mbamt.server.entity;

import vn.com.mbbank.kanban.mbamt.server.enums.GroupType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "SYS_GROUP") // "GROUP" is a reserved keyword in SQL
@Data
public class SysGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IS_CHANGE_ROLE")
    private Boolean isChangeRole;

    @Column(name = "DELETED")
    private Boolean deleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "GROUP_TYPE")
    private GroupType groupType;
}