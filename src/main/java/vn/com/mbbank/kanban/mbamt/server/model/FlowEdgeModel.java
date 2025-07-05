package vn.com.mbbank.kanban.mbamt.server.model;

import vn.com.mbbank.kanban.mbamt.server.enums.ApprovalResultStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Model representing an edge in a flow graph.
 * It includes the source and target nodes, as well as parsed handles for both ends.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class FlowEdgeModel {

    @Builder.Default
    ChangeFlowNodeModel sourceNodeModel = new ChangeFlowNodeModel();
    @Builder.Default
    ChangeFlowNodeModel targetNodeModel = new ChangeFlowNodeModel();
    private String id;
    private Long changeFlowId;
    private ApprovalResultStatus resultStatus;

}