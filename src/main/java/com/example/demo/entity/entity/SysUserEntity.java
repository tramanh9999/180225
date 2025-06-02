package com.example.demo.entity.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "USERS") // "USER" is a reserved keyword in some SQL dialects
public class SysUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "EMAIL")
    private String email;

    @OneToMany(mappedBy = "user")
    private List<SysGroupUserEntity> groups;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<SysGroupUserEntity> getGroups() {
        return groups;
    }

    public void setGroups(List<SysGroupUserEntity> groups) {
        this.groups = groups;
    }
}