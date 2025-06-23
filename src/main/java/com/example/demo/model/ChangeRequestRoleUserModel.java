package com.example.demo.model;

import com.example.demo.entity.ChangeNodeEntity;
import com.example.demo.entity.ChangeRequestWorkflowEntity;
import lombok.*;

/**
 * Model representing a change request role user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChangeRequestRoleUserModel extends BaseModel {
    private Long id;
    private Long changeRequestRoleId;
    private String username;
    private Integer cabGroup;
    private Integer cabGroupOrder;


    // xác định node trong change workflow
    private Long changeRequestWorkflowDetailId;
    // là bao gồm 2 entity bên dưới
    private ChangeRequestWorkflowEntity changeRequestWorkflowId;
    private ChangeNodeEntity changeNodeEntity;

}
