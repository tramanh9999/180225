package vn.com.mbbank.kanban.mbamt.server.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CHANGE_TEMPLATE_ROLE_USER")
@Data
public class ChangeTemplateRoleUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CHANGE_TEMPLATE_ROLE_ID")
    private Long changeTemplateRoleId;

    @Column(name = "USERNAME")
    private String username;

    @ManyToOne
    @JoinColumn(name = "CHANGE_TEMPLATE_ROLE_ID", insertable = false, updatable = false)
    private ChangeTemplateRoleEntity changeTemplateRole;
}