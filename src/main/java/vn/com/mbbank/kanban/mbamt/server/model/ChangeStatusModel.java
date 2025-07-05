package vn.com.mbbank.kanban.mbamt.server.model;

import vn.com.mbbank.kanban.mbamt.server.enums.ChangeStage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStatusModel {
    public static final Long DRAFT = 0L;
    private Long id;
    private String name;
    private String description;
    private String action;
    private ChangeStage stage;
    private Date createdDate;
    private String createdBy;
    private String modifiedBy;
    private Date modifiedDate;

}
