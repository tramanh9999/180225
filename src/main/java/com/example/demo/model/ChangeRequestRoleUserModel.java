package com.example.demo.model;

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

}
