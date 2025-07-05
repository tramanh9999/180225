package vn.com.mbbank.kanban.mbamt.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import vn.com.mbbank.kanban.mbamt.server.entity.ChangeFlowEntity;
import vn.com.mbbank.kanban.mbamt.server.enums.ApprovalResultStatus;
import vn.com.mbbank.kanban.mbamt.server.enums.FlowConstants;
import vn.com.mbbank.kanban.mbamt.server.enums.HandleType;
import vn.com.mbbank.kanban.mbamt.server.enums.NodeType;
import vn.com.mbbank.kanban.mbamt.server.mapper.ChangeFlowNodeMapper;
import com.example.demo.model.*;
import vn.com.mbbank.kanban.mbamt.server.model.*;
import vn.com.mbbank.kanban.mbamt.server.demo.model.*;
import vn.com.mbbank.kanban.mbamt.server.repository.ChangeFlowRepository;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeFlowNodeService;
import vn.com.mbbank.kanban.mbamt.server.service.FlowManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static vn.com.mbbank.kanban.mbamt.server.enums.FlowConstants.*;
import static vn.com.mbbank.kanban.mbamt.server.enums.NodeType.*;
import static vn.com.mbbank.kanban.mbamt.server.model.ErrorCodeCommon.UNSUPPORTED_APPROVAL_STATUS;

/**
 * The type Flow management service.
 */
@Slf4j
@Service
public class FlowManagementServiceImpl implements FlowManagementService {

    public static final String FLOW_DATA_CACHE = "flowDataCache";
    private static final String KEY_DELIMITER = "::";
    @Autowired
    ChangeFlowNodeMapper changeFlowNodeMapper;
    @Autowired
    private ChangeFlowRepository changeFlowRepository; // Assume you have this repository
    @Autowired
    private ChangeFlowNodeService changeFlowNodeService;


    /**
     * Convert a list of FlowEdgeModel objects to a JSON string.
     *
     * @param edgeModels the list of FlowEdgeModel objects
     * @return the JSON string representation, or null if conversion fails
     */
    public static String convertFlowEdgesModelListToJson(List<FlowEdgeModel> edgeModels) {
        try {
            return JSON.toJSONString(edgeModels, SerializerFeature.PrettyFormat);
        } catch (Exception e) {
            log.error("Error converting List<FlowEdgeModel> to JSON string using Fastjson: {}",
                    e.getMessage(), e);
            return null;
        }
    }

    /**
     * Create a unique mapKey from the source node ID and source handle ID.
     *
     * @param sourceHandleId ID of the source handle (e.g., "Accept-output", "0-output").
     * @return The generated mapKey string.
     * @throws IllegalArgumentException if sourceNodeId or sourceHandleId is null or empty.
     */
    public static String createEdgeMapKey(String sourceHandleId) {
        return sourceHandleId;
    }

    /**
     * Parses the NodeType from a given node ID string (e.g., "APPROVAL_NODE-1750059604667").
     * This method is intended for actual node IDs.
     *
     * @param nodeId The ID string of the node.
     * @return The corresponding NodeType, or NodeType.UNKNOWN if no match is found.
     */
    public static NodeType parseTypeFromNodeId(String nodeId) {
        if (nodeId == null || nodeId.isEmpty()) {
            return UNKNOWN;
        }

        if (nodeId.equalsIgnoreCase(START.getValue())) {
            return START;
        }
        if (nodeId.equalsIgnoreCase(END.getValue())) {
            return END;
        }
        for (NodeType type : NodeType.values()) {
            if (type == START || type == END || type == UNKNOWN) {
                continue;
            }
            if (nodeId.startsWith(type.getValue() + "-")) {
                return type;
            }
        }
        return UNKNOWN;
    }

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
            //log nodes
            log.info("Indexed {} nodes for ChangeFlow ID: {}", indexedNodes.size(), changeFlowId);
            if (flowEdgesJson != null && !flowEdgesJson.trim().isEmpty()) {
                List<FlowEdgeModel> rawEdges = parseFlowEdgesFromJson(flowEdgesJson, indexedNodes);
                indexedEdgesBySourceAndHandle = createFastLookupEdgeMap(rawEdges);
                log.info("Indexed {} edges for ChangeFlow ID: {}",
                        indexedEdgesBySourceAndHandle.size(), changeFlowId);
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
    public String buildHandleOutputIdForApprovalAction(String nodeId, ApprovalResultStatus status) {

        String result;
        if (nodeId == null) {
            return FlowConstants.START_NODE_SOURCE_HANDLE_ID;
        }

        if (ApprovalResultStatus.ACCEPT == status) {
            result = nodeId + "-Accept-output";
        } else if (ApprovalResultStatus.REJECT == status) {
            result = nodeId + "-Reject-output";
        } else {
            throw new BusinessException(UNSUPPORTED_APPROVAL_STATUS, status);
        }
        return result;
    }

    /**
     * Parse a JSON string into a list of FlowEdgeModel objects.
     *
     * @param jsonString   the JSON string to parse
     * @param indexedNodes
     * @return a list of FlowEdgeModel objects, or an empty list if parsing fails
     */
    public List<FlowEdgeModel> parseFlowEdgesFromJson(String jsonString,
                                                      Map<String, ChangeFlowNodeModel> indexedNodes) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            List<FlowEdgeRawModel> flowEdgeRawModels =
                    JSON.parseObject(jsonString, new TypeReference<>() {
                    });
            // Convert raw edges to FlowEdgeModel using the provided indexed nodes
            return flowEdgeRawModels.stream()
                    .map(rawEdge -> fromSimpleFlowEdge(rawEdge, indexedNodes)).toList();
        } catch (Exception e) {
            log.error("Error parsing JSON string to List<FlowEdgeModel> using Fastjson: {}",
                    e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public ParsedNodeHandleModel parseHandleString(String handleString, NodeType nodeType) {


        if (START.equals(nodeType)) {
            handleString = FlowConstants.START_NODE_SOURCE_HANDLE_ID;
        }
        if (handleString == null || handleString.isEmpty()) {
            return new ParsedNodeHandleModel(null, null, null, null, ApprovalResultStatus.UNKNOWN);
        }

        ParsedNodeHandleModel parsedHandle = new ParsedNodeHandleModel();
        parsedHandle.setRawHandleId(handleString);
        parsedHandle.setType(HandleType.fromHandleIdString(handleString));

        Matcher numericMatcher = CHANGE_STATUS_ID_HANDLE_PATTERN.matcher(handleString);
        Matcher customApprovalMatcher = NODE_APPROVAL_HANDLE_PATTERN.matcher(handleString);
        Matcher nodeMatcher = NODE_ID_HANDLE_PATTERN.matcher(handleString);

        if (numericMatcher.matches()) {
            try {
                parsedHandle.setChangeStatusId(Long.parseLong(numericMatcher.group(
                        CHANGE_STATUS_ID_HANDLE_PATTERN__CHANGE_STATUS_ID_INDEX)));
            } catch (NumberFormatException e) {
                log.error("Failed to parse change status ID from numeric handle: '{}'. Error: {}",
                        handleString, e.getMessage());
            }
        } else if (customApprovalMatcher.matches()) {
            parsedHandle.setApprovedAction(ApprovalResultStatus.fromValue(
                            customApprovalMatcher.group(
                                    NODE_APPROVAL_HANDLE_PATTERN__NODE_APPROVAL_ACTION_INDEX))
                    .orElse(ApprovalResultStatus.UNKNOWN));
        } else if (nodeMatcher.matches()) {
            parsedHandle.setType(nodeMatcher.group(NODE_ID_HANDLE_PATTERN__HANDLE_TYPE_INDEX)
                    .equalsIgnoreCase(FlowConstants.INPUT_KEYWORD) ? HandleType.INPUT :
                    HandleType.OUTPUT);
        } else {
            log.debug("Handle string '{}' did not match any known patterns. " +
                    "Type remains as determined by fromHandleIdString or defaulted.", handleString);
            throw new BusinessException(ErrorCodeCommon.INVALID_HANDLE_FORMAT, handleString);
        }

        return parsedHandle;
    }

    /**
     * Converts a simple FlowEdgeRawModel to a FlowEdgeModel.
     * This method is used to convert raw edge data into a model that includes parsed handles.
     *
     * @param rawEdge the raw edge data to convert
     * @return a FlowEdgeModel with parsed handles
     */
    public FlowEdgeModel fromSimpleFlowEdge(FlowEdgeRawModel rawEdge,
                                            Map<String, ChangeFlowNodeModel> indexedNodes) {
        FlowEdgeModel model = new FlowEdgeModel();
        model.setId(rawEdge.getId());
        model.setSourceNodeModel(changeFlowNodeMapper.cloneModel(
                indexedNodes.getOrDefault(rawEdge.getSource(), new ChangeFlowNodeModel())));
        ChangeFlowNodeModel sourceNode = model.getSourceNodeModel();
        sourceNode.setParsedType(parseTypeFromNodeId(rawEdge.getSource()));
        sourceNode.setParsedHandle(
                parseHandleString(rawEdge.getSourceHandle(), sourceNode.getParsedType()));

        model.setTargetNodeModel(
                changeFlowNodeMapper.cloneModel(indexedNodes.get(rawEdge.getTarget())));
        // check if the target node found then set parsed type = NodeType.parseTypeFromNodeId(rawEdge.getTarget())
        var targetNode = model.getTargetNodeModel();
        if (targetNode != null) {
            targetNode.setParsedType(parseTypeFromNodeId(rawEdge.getTarget()));
            targetNode.setParsedHandle(
                    parseHandleString(rawEdge.getTargetHandle(), targetNode.getParsedType()));
        }

        return model;
    }

}
