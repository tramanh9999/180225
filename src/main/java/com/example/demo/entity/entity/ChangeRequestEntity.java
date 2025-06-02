package com.example.demo.entity.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CHANGE_REQUEST")
@Data
public class ChangeRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "DELETED")
    private Integer deleted;

    @Column(name = "CHANGE_TEMPLATE_ID")
    private Long changeTemplateId;
}