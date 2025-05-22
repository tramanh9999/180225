package com.example.demo.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public abstract class BaseDto<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime createdDate;

    private String createdBy;

    private LocalDateTime modifiedDate;

    private String modifiedBy;
}
