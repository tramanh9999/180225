package com.example.demo.service.impl;

import com.example.demo.entity.ChangeFlowEntity;
import com.example.demo.model.*;
import com.example.demo.repository.ChangeFlowRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.model.FlowDataParser.createMapKey;

/**
 * The type Flow management service.
 */
@Slf4j
@Service
public class FlowManagementService {

    public static final String FLOW_DATA_CACHE = "flowDataCache";

    // Delimiter used when creating mapKey


    @Autowired
    private ChangeFlowRepository changeFlowRepository; // Assume you have this repository


    /**
     * Create a Map for fast lookup of FlowEdgeModel by source node ID and source handle ID.
     * The key of the Map will be "sourceNodeId::sourceHandleId".
     * This function assumes that each (sourceNodeId, sourceHandleId) pair
     * maps to ONLY ONE FlowEdgeModel. If there are duplicate edges for the same key,
     * the last processed edge will OVERWRITE previous ones.</p>
     *
     * @param edgesList List of FlowEdgeModel.
     * @return Map with key as String (sourceId::sourceHandleId) and value as FlowEdgeModel.
     */
    public static Map<String, FlowEdgeModel> createFastLookupEdgeMap(
            List<FlowEdgeModel> edgesList) {
        Map<String, FlowEdgeModel> edgeMap = new HashMap<>();

        for (FlowEdgeModel edgeModel : edgesList) {
            String sourceId = edgeModel.getSource();
            String sourceHandleId = (edgeModel.getSourceHandle() != null) ?
                    edgeModel.getSourceHandle().getRawHandleId() : null;
            String mapKey = createMapKey(sourceId, sourceHandleId);
            edgeMap.put(mapKey, edgeModel);
        }
        return edgeMap;
    }

    //    @Cacheable(value = FLOW_DATA_CACHE, key = "'CHANGE_FLOW_' + #changeFlowId")
    public IndexedChangeFlowDataModel getFlowDataByChangeFlowId(Long changeFlowId) {
        log.info("Fetching flow data from database for ID: {}", changeFlowId);

        Optional<ChangeFlowEntity> optionalEntity = changeFlowRepository.findById(changeFlowId);

        if (optionalEntity.isPresent()) {
            ChangeFlowEntity entity = optionalEntity.get();
            String flowNodesJson = entity.getFlowNodes();
            String flowEdgesJson = entity.getFlowEdges();

            List<FlowNodeModel> nodes = Collections.emptyList();
            Map<String, FlowNodeModel> indexedNodes = Collections.emptyMap();

            // New Map structure for edges
            Map<String, FlowEdgeModel> indexedEdgesBySourceAndHandle = Collections.emptyMap();

            if (flowNodesJson != null && !flowNodesJson.trim().isEmpty()) {
                nodes = FlowDataParser.parseFlowNodesFromJson(flowNodesJson);
                // Build map for nodes
                indexedNodes = nodes.stream()
                        .collect(Collectors.toMap(FlowNodeModel::getId, // Key is node ID
                                node -> node,        // Value is FlowNodeModel
                                (existing, replacement) -> existing));
            }
            if (flowEdgesJson != null && !flowEdgesJson.trim().isEmpty()) {
                List<FlowEdgeModel> rawEdges = FlowDataParser.parseFlowEdgesFromJson(flowEdgesJson);
                indexedEdgesBySourceAndHandle = createFastLookupEdgeMap(rawEdges);
            }

            return new IndexedChangeFlowDataModel(indexedEdgesBySourceAndHandle, indexedNodes);
        }
        return new IndexedChangeFlowDataModel(Collections.emptyMap(), Collections.emptyMap());
    }

    /**
     * Saves flowNodes and flowEdges data as JSON strings to ChangeFlowEntity.
     * After saving, the corresponding cache entry will be evicted to ensure the latest data is loaded,
     * BUT ONLY IF AN IMPORTANT CHANGE IS DETECTED.
     *
     * @param changeFlowId ID of the ChangeFlowEntity to update.
     * @param nodes        Data FlowNode as List<FlowNodeDTO>.
     * @param edges        Data FlowEdge as List<FlowEdgeDTO>.
     * @return A SaveResult object containing the updated ChangeFlowEntity and a flag
     * indicating if an important change occurred.
     */
    @CacheEvict(value = FLOW_DATA_CACHE, key = "'CHANGE_FLOW_' + #changeFlowId", allEntries = false,
            condition = "#result.importantChangeOccurred")
    public ChangeFlowEntity saveFlowDataToChangeFlow(Long changeFlowId, List<FlowNodeModel> nodes,
                                                     List<FlowEdgeModel> edges) {
        log.info("Saving flow data to database for ID: {}", changeFlowId);

        IndexedChangeFlowDataModel oldFlowData = getFlowDataByChangeFlowId(changeFlowId);

        Optional<ChangeFlowEntity> optionalEntity = changeFlowRepository.findById(changeFlowId);

        if (optionalEntity.isPresent()) {
            ChangeFlowEntity entity = optionalEntity.get();

            String flowNodesJson = FlowDataParser.convertFlowNodesDtoListToJson(nodes);
            entity.setFlowNodes(flowNodesJson);

            String flowEdgesJson = FlowDataParser.convertFlowEdgesDtoListToJson(edges);
            entity.setFlowEdges(flowEdgesJson);

            ChangeFlowEntity updatedEntity = changeFlowRepository.save(entity);
            log.info("Data saved for ID: {}", changeFlowId);

            // 2. Create new data DTO from input lists (to compare with old data)
            IndexedChangeFlowDataModel newFlowData =
                    IndexedChangeFlowDataModel.buildFromLists(nodes, edges);
            return updatedEntity;
        }
        log.error("ChangeFlowEntity not found with id: {}", changeFlowId);
        throw new BusinessException(ErrorCodeCommon.CHANGE_FLOW_NOT_FOUND, changeFlowId);
    }


    /**
     * Finds the current FlowNode and potential next FlowNode(s) based on
     * the current node's ID and the handle ID from which the flow is exiting.
     *
     * @param changeFlowId
     * @param currentNodeId   The ID of the current FlowNode (e.g., "APPROVAL_NODE-1750059604667").
     * @param currentHandleId The raw ID of the handle from which the flow is exiting
     *                        (e.g., "APPROVAL_NODE-1750059604667-Accept-output", "0-output").
     * @return A container object (e.g., a custom DTO or a Map) holding the current node and a list of next nodes.
     * Returns an empty result if the current node or next nodes are not found.
     */
    public FlowNavigationInfo findCurrentAndNextNodeByCurrentPoint(Long changeFlowId,
                                                                   String currentNodeId,
                                                                   String currentHandleId) {
        // 1. Get the indexed flow data (nodes and edges)
        IndexedChangeFlowDataModel flowData = getFlowDataByChangeFlowId(changeFlowId);

        Map<String, FlowNodeModel> indexedNodes = flowData.getIndexedNodes();
        Map<String, FlowEdgeModel> indexedEdges = flowData.getIndexedEdges();

        // 2. Find the Current FlowNode
        FlowNodeModel currentFlowNode = indexedNodes.get(currentNodeId);

        if (currentFlowNode == null) {
            log.warn("Current FlowNode with ID {} not found.", currentNodeId);
        }

        String edgeLookupKey = createMapKey(currentNodeId, currentHandleId);
        FlowEdgeModel flowEdgeModel = indexedEdges.get(edgeLookupKey);

        String targetNodeId = flowEdgeModel.getTarget();
        FlowNodeModel targetNode = indexedNodes.get(targetNodeId);
        return new FlowNavigationInfo(flowEdgeModel, targetNode);
    }
}
