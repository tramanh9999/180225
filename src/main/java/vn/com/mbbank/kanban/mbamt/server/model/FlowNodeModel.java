package vn.com.mbbank.kanban.mbamt.server.model;

import vn.com.mbbank.kanban.mbamt.server.enums.NodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing a flow node in a workflow system.
 * It includes an ID, type, data associated with the node.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlowNodeModel {
    private String id;
    private NodeType parsedType;
}
