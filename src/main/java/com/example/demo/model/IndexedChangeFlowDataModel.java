package com.example.demo.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.demo.service.impl.FlowManagementService.createFastLookupEdgeMap;


@Data
// DTO nội bộ để trả về cả nodes, edges và Map tối ưu
public class IndexedChangeFlowDataModel {
    private Map<String, FlowEdgeModel> indexedEdges;
    private Map<String, FlowNodeModel> indexedNodes;

    public IndexedChangeFlowDataModel(Map<String, FlowEdgeModel> indexedEdges,
                                      Map<String, FlowNodeModel> indexedNodes) {
        this.indexedEdges = indexedEdges;
        this.indexedNodes = indexedNodes;
    }


    // Factory method to build FlowDataDTO from raw lists

    /**
     * Factory method to build IndexedChangeFlowDataModel from raw lists of nodes and edges.
     * This method now uses the improved edge indexing logic.
     *
     * @param nodes    A list of FlowNodeModel objects.
     * @param rawEdges A list of raw FlowEdge objects (as parsed from JSON).
     * @return An IndexedChangeFlowDataModel instance with indexed nodes and edges.
     */
    public static IndexedChangeFlowDataModel buildFromLists(List<FlowNodeModel> nodes,
                                                            List<FlowEdgeModel> rawEdges) { //
        // Changed to List<FlowEdge> as input for parsing

        Map<String, FlowEdgeModel> indexedEdges = Collections.emptyMap();
        if (rawEdges != null && !rawEdges.isEmpty()) {
            // *** This is where we use the createFastLookupEdgeMap logic ***
            indexedEdges = createFastLookupEdgeMap(rawEdges);
        }

        Map<String, FlowNodeModel> indexedNodes = Collections.emptyMap();
        if (nodes != null && !nodes.isEmpty()) {
            indexedNodes = nodes.stream().collect(
                    Collectors.toMap(FlowNodeModel::getId, node -> node,
                            (existing, replacement) -> existing));
        }
        return new IndexedChangeFlowDataModel(indexedEdges, indexedNodes);
    }

    public List<FlowNodeModel> getNodes() {
        return indexedNodes != null ? new ArrayList<>(indexedNodes.values()) :
                Collections.emptyList();
    }

    public List<FlowEdgeModel> getEdges() {
        return indexedEdges != null ? new ArrayList<>(indexedEdges.values()) :
                Collections.emptyList();
    }

}