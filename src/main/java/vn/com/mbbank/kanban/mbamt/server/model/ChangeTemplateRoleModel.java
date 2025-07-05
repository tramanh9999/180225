package vn.com.mbbank.kanban.mbamt.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeTemplateRoleModel {
    private Long id;
    private Long changeTemplateId;
    private Long groupId;
    private Integer level;
    private Integer roleOrder;
    private List<String> users;
}