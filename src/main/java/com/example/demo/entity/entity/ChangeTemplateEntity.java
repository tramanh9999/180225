package com.example.demo.entity.entity;

import java.util.List;

import com.example.demo.entity.BaseEntity;

import jakarta.persistence.*;


@Entity
@Table(name = "CHANGE_TEMPLATE")
public class ChangeTemplateEntity extends BaseEntity<Long> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Integer deleted;

    @OneToMany(mappedBy = "changeTemplate")
    private List<ChangeTemplateRoleEntity> roles;

    @OneToMany(mappedBy = "template")
    private List<ChangeRequestEntity> changeRequests;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public List<ChangeTemplateRoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<ChangeTemplateRoleEntity> roles) {
        this.roles = roles;
    }

    public List<ChangeRequestEntity> getChangeRequests() {
        return changeRequests;
    }

    public void setChangeRequests(List<ChangeRequestEntity> changeRequests) {
        this.changeRequests = changeRequests;
    }
}