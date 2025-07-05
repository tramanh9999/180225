package vn.com.mbbank.kanban.mbamt.server.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;


/**
 * Model representing indexed change flow data.
 * It contains indexed edges and nodes, providing methods to retrieve them as lists.
 */
@Data
@NoArgsConstructor
@Builder
public class IndexedChangeFlowDataModel {
    @Builder.Default
    private Map<String, FlowEdgeModel> indexedEdges = new HashMap<>();
    @Builder.Default
    private Map<String, ChangeFlowNodeModel> indexedNodes = new HashMap<>();
    ;

    public IndexedChangeFlowDataModel(Map<String, FlowEdgeModel> indexedEdges,
                                      Map<String, ChangeFlowNodeModel> indexedNodes) {
        this.indexedEdges = indexedEdges;
        this.indexedNodes = indexedNodes;
    }

}