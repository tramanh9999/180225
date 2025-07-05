package vn.com.mbbank.kanban.mbamt.server.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime createdDate;

    private String createdBy;

    private LocalDateTime modifiedDate;

    private String modifiedBy;
}
