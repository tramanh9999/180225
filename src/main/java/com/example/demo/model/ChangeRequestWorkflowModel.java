package com.example.demo.model;

import lombok.*;


/**
 * Model representing a change request workflow.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRequestWorkflowModel extends BaseModel {
    private Long id;
    private Long changeId;
    private String workflowData;
}
