package vn.com.mbbank.kanban.mbamt.server.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "GROUP_USER")
@Data
public class SysGroupUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "GROUP_ID")
    private Long groupId;

    @Column(name = "USER_ID")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID", insertable = false, updatable = false)
    private SysGroupEntity group;

    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private SysUserEntity user;
}