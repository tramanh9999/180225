package com.example.demo.service.impl;

import com.example.demo.entity.ChangeFlowEntity;
import com.example.demo.enums.ApprovalResultStatus;
import com.example.demo.enums.FlowConstants;
import com.example.demo.model.*;
import com.example.demo.repository.ChangeFlowRepository;
import com.example.demo.service.ChangeFlowNodeService;
import com.example.demo.service.FlowManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.model.ChangeFlowDataParser.createEdgeMapKey;
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
    @Autowired
    private ChangeFlowNodeService changeFlowNodeService;


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
            String sourceHandleId =
                    edgeModel.getSourceNodeModel().getParsedHandle().getRawHandleId();

            String mapKey = createEdgeMapKey(sourceHandleId);
            edgeMap.put(mapKey, edgeModel);
        }
        return edgeMap;
    }


    @Override
    public IndexedChangeFlowDataModel getFlowDataByChangeFlowId(Long changeFlowId) {
        log.info("Fetching flow data from database for ID: {}", changeFlowId);
        Optional<ChangeFlowEntity> optionalEntity = changeFlowRepository.findById(changeFlowId);
        if (optionalEntity.isPresent()) {
            ChangeFlowEntity entity = optionalEntity.get();
            String flowEdgesJson = entity.getFlowEdges();
            Map<String, FlowEdgeModel> indexedEdgesBySourceAndHandle = Collections.emptyMap();

            List<ChangeFlowNodeModel> changeFlowNodes =
                    changeFlowNodeService.findByChangeFlowId(changeFlowId);

            if (changeFlowNodes == null || changeFlowNodes.isEmpty()) {
                throw new BusinessException(ErrorCodeCommon.CHANGE_FLOW_NODES_NOT_FOUND,
                        changeFlowId);
            }
            Map<String, ChangeFlowNodeModel> indexedNodes = changeFlowNodes.stream().collect(
                    Collectors.toMap(ChangeFlowNodeModel::getNodeId, node -> node,
                            (existing, replacement) -> existing));
            if (flowEdgesJson != null && !flowEdgesJson.trim().isEmpty()) {
                List<FlowEdgeModel> rawEdges =
                        ChangeFlowDataParser.parseFlowEdgesFromJson(flowEdgesJson, indexedNodes);
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

    @CacheEvict(value = FLOW_DATA_CACHE, key = "'CHANGE_FLOW_' + #changeFlowId")
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
     * @param currentSourceHandleId The raw ID of the handle from which the flow is exiting
     *                              (e.g., "APPROVAL_NODE-1750059604667-Accept-output", "0-output").
     * @return A container object (e.g., a custom DTO or a Map) holding the current node and a list of next nodes.
     * Returns an empty result if the current node or next nodes are not found.
     */
    @Override
    public FlowEdgeModel getOrDefaultEdgeByNodeHandleId(String currentSourceHandleId,
                                                        IndexedChangeFlowDataModel flowDataModel) {
        String edgeLookupKey = createEdgeMapKey(currentSourceHandleId);
        Map<String, FlowEdgeModel> indexedEdges = flowDataModel.getIndexedEdges();
        return indexedEdges.get(edgeLookupKey);
    }


    @Override
    public String buildHandleOutputIdForApprovalAction(Long currentApprovalNodeId,
                                                       ApprovalResultStatus status) {

        String result;
        if (currentApprovalNodeId == null) {
            return FlowConstants.START_NODE_SOURCE_HANDLE_ID;
        }

        if (ApprovalResultStatus.ACCEPT == status) {
            result = currentApprovalNodeId + "-Accept-output";
        } else if (ApprovalResultStatus.REJECT == status) {
            result = currentApprovalNodeId + "-Reject-output";
        } else {
            throw new BusinessException(UNSUPPORTED_APPROVAL_STATUS, status);
        }
        return result;
    }


}
