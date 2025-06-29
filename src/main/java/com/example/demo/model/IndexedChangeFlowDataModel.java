package com.example.demo.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


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


    public List<FlowNodeModel> getNodes() {
        return indexedNodes != null ? new ArrayList<>(indexedNodes.values()) :
                Collections.emptyList();
    }

    public List<FlowEdgeModel> getEdges() {
        return indexedEdges != null ? new ArrayList<>(indexedEdges.values()) :
                Collections.emptyList();
    }

}