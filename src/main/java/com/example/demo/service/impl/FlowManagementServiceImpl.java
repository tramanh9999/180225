package com.example.demo.service.impl;

import com.example.demo.entity.ChangeFlowEntity;
import com.example.demo.enums.ApprovalResultStatus;
import com.example.demo.enums.FlowConstants;
import com.example.demo.enums.NodeType;
import com.example.demo.model.*;
import com.example.demo.repository.ChangeFlowRepository;
import com.example.demo.service.FlowManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.model.ChangeFlowDataParser.createMapKey;
import static com.example.demo.model.ErrorCodeCommon.UNSUPPORTED_APPROVAL_STATUS;

/**
 * The type Flow management service.
 */
@Slf4j
@Service
public class FlowManagementServiceImpl implements FlowManagementService {

    public static final String FLOW_DATA_CACHE = "flowDataCache";
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
    @Override
    public Map<String, FlowEdgeModel> createFastLookupEdgeMap(List<FlowEdgeModel> edgesList) {
        Map<String, FlowEdgeModel> edgeMap = new HashMap<>();

        for (FlowEdgeModel edgeModel : edgesList) {
            String sourceHandleId = (edgeModel.getSourceHandle() != null) ?
                    edgeModel.getSourceHandle().getRawHandleId() : null;
            if (FlowConstants.START_NODE_ID.equals(edgeModel.getSourceNodeId())) {
                sourceHandleId = FlowConstants.START_NODE_SOURCE_HANDLE_ID;
            }
            String mapKey = createMapKey(sourceHandleId);
            edgeMap.put(mapKey, edgeModel);
        }
        return edgeMap;
    }

    /**
     * Factory method to build IndexedChangeFlowDataModel from raw lists of nodes and edges.
     * This method now uses the improved edge indexing logic.
     *
     * @param nodes    A list of FlowNodeModel objects.
     * @param rawEdges A list of raw FlowEdge objects (as parsed from JSON).
     * @return An IndexedChangeFlowDataModel instance with indexed nodes and edges.
     */
    @Override
    public IndexedChangeFlowDataModel buildFromLists(List<FlowNodeModel> nodes,
                                                     List<FlowEdgeModel> rawEdges) { //
        Map<String, FlowEdgeModel> indexedEdges = Collections.emptyMap();
        if (rawEdges != null && !rawEdges.isEmpty()) {
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


    @Override
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
                nodes = ChangeFlowDataParser.parseFlowNodesFromJson(flowNodesJson);
                // Build map for nodes
                indexedNodes = nodes.stream()
                        .collect(Collectors.toMap(FlowNodeModel::getId, // Key is node ID
                                node -> node,        // Value is FlowNodeModel
                                (existing, replacement) -> existing));
            }
            if (flowEdgesJson != null && !flowEdgesJson.trim().isEmpty()) {
                List<FlowEdgeModel> rawEdges =
                        ChangeFlowDataParser.parseFlowEdgesFromJson(flowEdgesJson);
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
    @Override
    public ChangeFlowEntity saveFlowDataToChangeFlow(Long changeFlowId, List<FlowNodeModel> nodes,
                                                     List<FlowEdgeModel> edges) {
        log.info("Saving flow data to database for ID: {}", changeFlowId);
        Optional<ChangeFlowEntity> optionalEntity = changeFlowRepository.findById(changeFlowId);
        if (optionalEntity.isPresent()) {
            ChangeFlowEntity entity = optionalEntity.get();

            String flowNodesJson = ChangeFlowDataParser.convertFlowNodesModelListToJson(nodes);
            entity.setFlowNodes(flowNodesJson);

            String flowEdgesJson = ChangeFlowDataParser.convertFlowEdgesModelListToJson(edges);
            entity.setFlowEdges(flowEdgesJson);

            ChangeFlowEntity updatedEntity = changeFlowRepository.save(entity);
            log.info("Data saved for ID: {}", changeFlowId);
            return updatedEntity;
        }
        log.error("ChangeFlowEntity not found with id: {}", changeFlowId);
        throw new BusinessException(ErrorCodeCommon.CHANGE_FLOW_NOT_FOUND, changeFlowId);
    }


    /**
     * Finds the current FlowNode and potential next FlowNode(s) based on
     * the current node's ID and the handle ID from which the flow is exiting.
     *
     * @param changeFlowId          The ID of the ChangeFlow
     * @param currentSourceHandleId The raw ID of the handle from which the flow is exiting
     *                              (e.g., "APPROVAL_NODE-1750059604667-Accept-output", "0-output").
     * @return A container object (e.g., a custom DTO or a Map) holding the current node and a list of next nodes.
     * Returns an empty result if the current node or next nodes are not found.
     */
    @Override
    public FlowEdgeModel getOrDefaultEdgeByNodeHandleId(Long c, String currentSourceHandleId,
                                                        Map<String, FlowEdgeModel> indexedEdges) {
        String edgeLookupKey = createMapKey(currentSourceHandleId);
        FlowEdgeModel edgeModel = indexedEdges.get(edgeLookupKey);
        if (edgeModel == null) {
            edgeModel = FlowEdgeModel.builder().sourceNodeId(currentSourceHandleId).sourceNode(
                            FlowNodeModel.builder().id(currentSourceHandleId)
                                    .type(NodeType.parseTypeFromNodeHandleId(currentSourceHandleId))
                                    .build())
                    .sourceHandle(FlowEdgeModel.parseHandleString(currentSourceHandleId)).build();
        }
        return edgeModel;
    }


    @Override
    public String createApprovalNodeHandleIdFromNodeIdAndStatus(String currentApprovalNodeId,
                                                                ApprovalResultStatus status) {
        if (currentApprovalNodeId == null || currentApprovalNodeId.isEmpty()) {
            currentApprovalNodeId = FlowConstants.START_NODE_SOURCE_HANDLE_ID;
        }

        String currentChangeFlowNodeHandleId;
        if (ApprovalResultStatus.ACCEPT == status) {
            currentChangeFlowNodeHandleId = currentApprovalNodeId + "-Accept-output";
        } else if (ApprovalResultStatus.REJECT == status) {
            currentChangeFlowNodeHandleId = currentApprovalNodeId + "-Reject-output";
        } else {
            throw new BusinessException(UNSUPPORTED_APPROVAL_STATUS, status);
        }
        return currentChangeFlowNodeHandleId;
    }


}
