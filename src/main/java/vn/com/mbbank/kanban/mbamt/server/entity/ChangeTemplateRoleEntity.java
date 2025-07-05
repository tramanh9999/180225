package vn.com.mbbank.kanban.mbamt.server.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CHANGE_TEMPLATE_ROLE")
@Data
public class ChangeTemplateRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CHANGE_TEMPLATE_ID")
    private Long changeTemplateId;

    @Column(name = "GROUP_ID")
    private Long groupId;

    @Column(name = "LEVEL")
    private Integer level;

    @Column(name = "ROLE_ORDER")
    private Integer roleOrder;

    @ManyToOne
    @JoinColumn(name = "CHANGE_TEMPLATE_ID", insertable = false, updatable = false)
    private ChangeTemplateEntity changeTemplate;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID", insertable = false, updatable = false)
    private SysGroupEntity group;
}