package vn.com.mbbank.kanban.mbamt.server.model;

import lombok.Data;

@Data
public class ChangeWorkflowNodeModel extends BaseModel {
    private Long id;
    private String nodeName;
}