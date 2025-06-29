package com.example.demo.model;

import com.example.demo.enums.NodeType;
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
    private NodeType type;
    private FlowNodeDataModel data;

}
