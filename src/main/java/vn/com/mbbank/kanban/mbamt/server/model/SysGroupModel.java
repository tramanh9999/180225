package vn.com.mbbank.kanban.mbamt.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysGroupModel {
    private Long id;
    private String name;
    private String description;
    private Boolean isChangeRole;
    private String groupType;
}
