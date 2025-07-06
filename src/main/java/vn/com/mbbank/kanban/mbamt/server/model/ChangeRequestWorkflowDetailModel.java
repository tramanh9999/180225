package vn.com.mbbank.kanban.mbamt.server.model;

import lombok.*;

// No need to extend BaseModel if it's purely a DTO for service layer
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Using @Builder for DTOs
public class ChangeRequestWorkflowDetailModel {
    private Long id;
    private Long changeRequestWorkflowId;
    private Long changeNodeId;

    private DetailSettingsModel detailSettingsModel;

    private ChangeWorkflowNodeModel changeNodeModel;
    private ChangeRequestWorkflowModel changeRequestWorkflowModel;
    // Add any other fields that might be relevant for your business logic
    // even if they are not directly in the entity (e.g., nodeName, workflowName if enriched)
}