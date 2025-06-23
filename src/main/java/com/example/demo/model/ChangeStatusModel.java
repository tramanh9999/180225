package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStatusModel {
    private Long id;
    private String name;
    private String description;
    private String action;
    private String stage;
    private Date createdDate;
    private String createdBy;
    private String modifiedBy;
    private Date modifiedDate;

}
