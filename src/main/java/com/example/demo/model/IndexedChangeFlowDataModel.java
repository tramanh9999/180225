package com.example.demo.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


/**
 * Model representing indexed change flow data.
 * It contains indexed edges and nodes, providing methods to retrieve them as lists.
 */
@Data
@NoArgsConstructor
@Builder
public class IndexedChangeFlowDataModel {
    private Map<String, FlowEdgeModel> indexedEdges;
    private Map<String, FlowNodeModel> indexedNodes;

    public IndexedChangeFlowDataModel(Map<String, FlowEdgeModel> indexedEdges,
                                      Map<String, FlowNodeModel> indexedNodes) {
        this.indexedEdges = indexedEdges;
        this.indexedNodes = indexedNodes;
    }

}