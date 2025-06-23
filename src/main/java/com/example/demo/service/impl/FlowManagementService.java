package com.example.demo.service.impl;

import com.example.demo.entity.ChangeFlowEntity;
import com.example.demo.enums.NodeType;
import com.example.demo.model.*;
import com.example.demo.repository.ChangeFlowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlowManagementService {

    public static final String FLOW_DATA_CACHE = "flowDataCache";
    @Autowired
    private ChangeFlowRepository changeFlowRepository; // Giả sử bạn có repository này

    /**
     * Lưu dữ liệu flowNodes và flowEdges dưới dạng JSON string vào ChangeFlowEntity.
     *
     * @param changeFlowId ID của ChangeFlowEntity cần cập nhật.
     * @param nodes        Dữ liệu FlowNode dưới dạng List<FlowNodeDTO>.
     * @param edges        Dữ liệu FlowEdge dưới dạng List<FlowEdgeDTO>.
     * @return ChangeFlowEntity đã được cập nhật.
     */
    public ChangeFlowEntity saveFlowDataToChangeFlowSimple(Long changeFlowId,
                                                           List<FlowNodeModel> nodes,
                                                           List<FlowEdgeModel> edges) {
        Optional<ChangeFlowEntity> optionalEntity = changeFlowRepository.findById(changeFlowId);

        if (optionalEntity.isPresent()) {
            ChangeFlowEntity entity = optionalEntity.get();

            // Chuyển đổi List<FlowNodeDTO> thành JSON string để lưu
            String flowNodesJson = FlowDataParser.convertFlowNodesDtoListToJson(nodes);
            entity.setFlowNodes(flowNodesJson);

            // Chuyển đổi List<FlowEdgeDTO> thành JSON string để lưu
            String flowEdgesJson = FlowDataParser.convertFlowEdgesDtoListToJson(edges);
            entity.setFlowEdges(flowEdgesJson);

            return changeFlowRepository.save(entity); // Lưu entity đã cập nhật
        }
        throw new RuntimeException("ChangeFlowEntity not found with id: " + changeFlowId);
    }

    public FlowNodeModel getNextNodeDetails(Long changeFlowId, Long inputChangeStatusId) {
        NextFlowTargetModel nextTarget =
                findNextTargetFromChangeStatusId(changeFlowId, inputChangeStatusId);

        if (nextTarget != null) {
            FlowDataDTO flowData = getFlowDataByChangeFlowId(changeFlowId);
            List<FlowNodeModel> nodes = flowData.getNodes();

            // Tìm FlowNodeDTO khớp với nextNodeId
            return nodes.stream().filter(node -> node.getId().equals(nextTarget.getNextNodeId()))
                    .findFirst().orElse(null);
        }
        return null; // Không tìm thấy node tiếp theo
    }

    /**
     * Lấy ChangeFlowEntity theo ID và parse dữ liệu flowNodes và flowEdges JSON của nó
     * thành các danh sách DTO tương ứng và Map optimized cho edges.
     *
     * @param changeFlowId ID của ChangeFlowEntity.
     * @return Một đối tượng FlowDataDTO chứa cả List<FlowNodeDTO>, List<FlowEdgeDTO>
     * và Map các edges được tối ưu, hoặc các danh sách rỗng nếu không tìm thấy entity.
     */
    public FlowDataDTO getFlowDataByChangeFlowIdSimple(Long changeFlowId) {
        Optional<ChangeFlowEntity> optionalEntity = changeFlowRepository.findById(changeFlowId);

        if (optionalEntity.isPresent()) {
            ChangeFlowEntity entity = optionalEntity.get();
            String flowNodesJson = entity.getFlowNodes();
            String flowEdgesJson = entity.getFlowEdges();

            List<FlowNodeModel> nodes = Collections.emptyList();
            Map<String, FlowNodeModel> indexedNodes = Collections.emptyMap();
            List<FlowEdgeModel> edges = Collections.emptyList();
            Map<Long, FlowEdgeModel> indexedEdges = Collections.emptyMap();

            if (flowNodesJson != null && !flowNodesJson.trim().isEmpty()) {
                nodes = FlowDataParser.parseFlowNodesFromJson(flowNodesJson);
                // Build map for nodes
                indexedNodes = nodes.stream()
                        .collect(Collectors.toMap(FlowNodeModel::getId, // Key is node ID
                                node -> node,        // Value is FlowNodeDTO
                                (existing, replacement) -> existing
                                // Handle duplicates (keep existing)
                        ));
            }
            if (flowEdgesJson != null && !flowEdgesJson.trim().isEmpty()) {
                edges = FlowDataParser.parseFlowEdgesFromJson(flowEdgesJson);

                indexedEdges = edges.stream().filter(edge -> edge.getSourceHandle() != null &&
                        edge.getSourceHandle().getChangeStatusId() != null).collect(
                        Collectors.toMap(edge -> edge.getSourceHandle().getChangeStatusId(),
                                edge -> edge, (existing, replacement) -> existing));
            }

            return new FlowDataDTO(indexedEdges, indexedNodes); // Pass only maps to constructor
        }
        return new FlowDataDTO(Collections.emptyMap(), Collections.emptyMap()); // Return empty maps

    }

    @Cacheable(value = FLOW_DATA_CACHE, key = "'CHANGE_FLOW_' + #changeFlowId")
    public FlowDataDTO getFlowDataByChangeFlowId(Long changeFlowId) {
        System.out.println("Fetching flow data from database for ID: " + changeFlowId);

        Optional<ChangeFlowEntity> optionalEntity = changeFlowRepository.findById(changeFlowId);

        if (optionalEntity.isPresent()) {
            ChangeFlowEntity entity = optionalEntity.get();
            String flowNodesJson = entity.getFlowNodes();
            String flowEdgesJson = entity.getFlowEdges();

            List<FlowNodeModel> nodes = Collections.emptyList();
            Map<String, FlowNodeModel> indexedNodes = Collections.emptyMap();
            List<FlowEdgeModel> edges = Collections.emptyList();
            Map<Long, FlowEdgeModel> indexedEdges = Collections.emptyMap();

            if (flowNodesJson != null && !flowNodesJson.trim().isEmpty()) {
                nodes = FlowDataParser.parseFlowNodesFromJson(flowNodesJson);
                // Build map for nodes
                indexedNodes = nodes.stream()
                        .collect(Collectors.toMap(FlowNodeModel::getId, // Key is node ID
                                node -> node,        // Value is FlowNodeDTO
                                (existing, replacement) -> existing
                                // Handle duplicates (keep existing)
                        ));
            }
            if (flowEdgesJson != null && !flowEdgesJson.trim().isEmpty()) {
                edges = FlowDataParser.parseFlowEdgesFromJson(flowEdgesJson);

                indexedEdges = edges.stream().filter(edge -> edge.getSourceHandle() != null &&
                        edge.getSourceHandle().getChangeStatusId() != null).collect(
                        Collectors.toMap(edge -> edge.getSourceHandle().getChangeStatusId(),
                                edge -> edge, (existing, replacement) -> existing));
            }

            return new FlowDataDTO(indexedEdges, indexedNodes); // Pass only maps to constructor
        }
        return new FlowDataDTO(Collections.emptyMap(), Collections.emptyMap()); // Return empty maps
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
    public SaveResult saveFlowDataToChangeFlow(Long changeFlowId, List<FlowNodeModel> nodes,
                                               List<FlowEdgeModel> edges) {
        System.out.println("Saving flow data to database for ID: " + changeFlowId);

        // 1. Get old data (might be from cache, this is important for comparison)
        FlowDataDTO oldFlowData = getFlowDataByChangeFlowId(changeFlowId);

        Optional<ChangeFlowEntity> optionalEntity = changeFlowRepository.findById(changeFlowId);

        if (optionalEntity.isPresent()) {
            ChangeFlowEntity entity = optionalEntity.get();

            String flowNodesJson = FlowDataParser.convertFlowNodesDtoListToJson(nodes);
            entity.setFlowNodes(flowNodesJson);

            String flowEdgesJson = FlowDataParser.convertFlowEdgesDtoListToJson(edges);
            entity.setFlowEdges(flowEdgesJson);

            ChangeFlowEntity updatedEntity = changeFlowRepository.save(entity);
            System.out.println("Data saved for ID: " + changeFlowId);

            // 2. Create new data DTO from input lists (to compare with old data)
            FlowDataDTO newFlowData = FlowDataDTO.buildFromLists(nodes, edges);

            // 3. Compare for important changes
            boolean importantChangeDetected = isImportantChange(oldFlowData, newFlowData);

            if (importantChangeDetected) {
                System.out.println("IMPORTANT CHANGE DETECTED for ID: " + changeFlowId +
                        ". Cache will be evicted.");
            } else {
                System.out.println("No important change detected for ID: " + changeFlowId +
                        ". Cache will NOT be evicted.");
            }

            return new SaveResult(updatedEntity, importantChangeDetected);
        }
        throw new RuntimeException("ChangeFlowEntity not found with id: " + changeFlowId);
    }

    /**
     * Finds the next target point (node and handle) based on an input changeStatusId.
     * This method uses the optimized Map for quick lookup.
     *
     * @param changeFlowId        ID of the ChangeFlowEntity this flow belongs to.
     * @param inputChangeStatusId ID of the ChangeStatus to find the next point from.
     * @return NextFlowTargetDTO containing the target node ID and target handle ID,
     * or null if no next path is found from this changeStatusId.
     */
    public NextFlowTargetModel findNextTargetFromChangeStatusId(Long changeFlowId,
                                                                Long inputChangeStatusId) {
        FlowDataDTO flowData = getFlowDataByChangeFlowId(changeFlowId);
        Map<Long, FlowEdgeModel> indexedEdges = flowData.getIndexedEdges();

        FlowEdgeModel foundEdge = indexedEdges.get(inputChangeStatusId);

        if (foundEdge != null) {
            return new NextFlowTargetModel(foundEdge.getTarget(),
                    foundEdge.getTargetHandle() != null ?
                            foundEdge.getTargetHandle().getRawHandleId() : null);
        }
        return null;
    }

    /**
     * Determines if the next target node in the flow, based on a given changeStatusId,
     * is an Approval Node or a CAB Node.
     *
     * @param changeFlowId        ID of the ChangeFlowEntity this flow belongs to.
     * @param inputChangeStatusId The changeStatusId from which to find the next target.
     * @return true if the next target is an APPROVAL_NODE or CAB_NODE,
     * false if the next target is a handle change status of another node, or
     * any other node type, or if no next target is found.
     */
    public boolean isNextTargetApprovalOrCabNode(Long changeFlowId, Long inputChangeStatusId) {
        NextFlowTargetModel nextTarget =
                findNextTargetFromChangeStatusId(changeFlowId, inputChangeStatusId);

        if (nextTarget == null || nextTarget.getNextNodeId() == null) {
            return false; // No next target found
        }

        FlowDataDTO flowData = getFlowDataByChangeFlowId(changeFlowId);
        // Use the new indexedNodes map for quick lookup
        FlowNodeModel nextNode = flowData.getIndexedNodes().get(nextTarget.getNextNodeId());

        if (nextNode != null) {
            // Check the node's type
            // Based on your NodeType enum, APPROVAL_NODE and CAB_NODE are specific types
            return nextNode.getType() == NodeType.APPROVAL_NODE ||
                    nextNode.getType() == NodeType.CAB_NODE;
        }

        return false; // Next node not found in the map (shouldn't happen if IDs are consistent)
    }


    /**
     * Calculates a hash string representing only the important changes in flow data.
     * This hash can be used for comparing two flow states to detect significant changes,
     * ignoring UI-related properties like position, style, selection state, etc.
     * <p>
     * Important changes include:
     * - Number of nodes and edges.
     * - Node identification data (ID, type, relevant data for pathing like statusList IDs, process node name/type).
     * - Edge path data (source, target, sourceHandle ID raw, targetHandle ID raw, edge type).
     *
     * @param flowData The FlowDataDTO object to calculate the hash for.
     * @return A String hash representing the important aspects of the flow data.
     */
    private String calculateFlowDataHash(FlowDataDTO flowData) {
        StringBuilder sb = new StringBuilder();

        // 1. Number of nodes and edges
        sb.append("NODES_COUNT:").append(flowData.getNodes().size()).append(";");
        sb.append("EDGES_COUNT:").append(flowData.getEdges().size()).append(";");

        // 2. Node identification data
        // Sort nodes by ID to ensure consistent hash generation regardless of order
        flowData.getNodes().stream().sorted(Comparator.comparing(FlowNodeModel::getId))
                .forEach(node -> {
                    sb.append("NODE_ID:").append(node.getId()).append("|");
                    sb.append("NODE_TYPE:")
                            .append(node.getType() != null ? node.getType().getPrefix() : "null")
                            .append("|");

                    // Important data fields for nodes
                    if (node.getData() != null) {
                        // For stage nodes (SUBMISSION_PLANNING, APPROVAL, etc.)
                        if (node.getData().getStageType() != null) {
                            sb.append("STAGE_TYPE:").append(node.getData().getStageType())
                                    .append("|");
                            if (node.getData().getStatusList() != null) {
                                // Include status IDs as they are crucial for edge connections
                                node.getData().getStatusList().stream()
                                        .sorted(Comparator.comparing(ChangeStatusModel::getId,
                                                Comparator.nullsFirst(
                                                        Long::compareTo))) // Sort statuses by ID
                                        .forEach(status -> sb.append("STATUS_ID:")
                                                .append(status.getId()).append(","));
                            }
                        }
                        // For process nodes (APPROVAL_NODE, CAB_NODE)
                        if (node.getData().getName() != null) {
                            sb.append("NODE_NAME:").append(node.getData().getName()).append("|");
                        }
                        if (node.getData().getType() != null) { // Internal type for process nodes
                            sb.append("PROCESS_NODE_TYPE:").append(node.getData().getType())
                                    .append("|");
                        }
                    }
                    sb.append(";"); // End of node data
                });

        // 3. Edge path data
        // Sort edges by ID to ensure consistent hash generation regardless of order
        flowData.getEdges().stream().sorted(Comparator.comparing(FlowEdgeModel::getId))
                .forEach(edge -> {
                    sb.append("EDGE_ID:").append(edge.getId()).append("|");
                    sb.append("EDGE_SOURCE:").append(edge.getSource()).append("|");
                    sb.append("EDGE_TARGET:").append(edge.getTarget()).append("|");
                    sb.append("EDGE_SOURCE_HANDLE_RAW:").append(edge.getSourceHandle() != null ?
                            edge.getSourceHandle().getRawHandleId() : "null").append("|");
                    sb.append("EDGE_TARGET_HANDLE_RAW:").append(edge.getTargetHandle() != null ?
                            edge.getTargetHandle().getRawHandleId() : "null").append("|");
                    sb.append("EDGE_TYPE:").append(edge.getType() != null ? edge.getType() : "null")
                            .append("|");
                    // Include changeStatusId and customAction from ParsedHandleDTO for path importance
                    if (edge.getSourceHandle() != null) {
                        sb.append("EDGE_SOURCE_CHANGE_STATUS_ID:")
                                .append(edge.getSourceHandle().getChangeStatusId()).append("|");
                        sb.append("EDGE_SOURCE_CUSTOM_ACTION:")
                                .append(edge.getSourceHandle().getCustomAction()).append("|");
                    }
                    if (edge.getTargetHandle() != null) {
                        sb.append("EDGE_TARGET_CHANGE_STATUS_ID:")
                                .append(edge.getTargetHandle().getChangeStatusId()).append("|");
                        sb.append("EDGE_TARGET_CUSTOM_ACTION:")
                                .append(edge.getTargetHandle().getCustomAction()).append("|");
                    }
                    sb.append(";"); // End of edge data
                });

        // For a more robust hash, use a cryptographic hash function like SHA-256
        // return Hashing.sha256().hashString(sb.toString(), StandardCharsets.UTF_8).toString();
        return sb.toString(); // Simple string concatenation for demonstration
    }

    /**
     * Compares two FlowDataDTO objects to determine if there are important changes
     * between them, ignoring UI-related properties.
     *
     * @param oldFlowData The old FlowDataDTO.
     * @param newFlowData The new FlowDataDTO.
     * @return true if important changes are detected, false otherwise.
     */
    public boolean isImportantChange(FlowDataDTO oldFlowData, FlowDataDTO newFlowData) {
        if (oldFlowData == null && newFlowData == null) {
            return false; // No change if both are null
        }
        if (oldFlowData == null || newFlowData == null) {
            return true; // Change if one is null and other is not
        }

        String oldHash = calculateFlowDataHash(oldFlowData);
        String newHash = calculateFlowDataHash(newFlowData);

        return !oldHash.equals(newHash);
    }

    // DTO nội bộ để trả về kết quả lưu và cờ thay đổi quan trọng
    public static class SaveResult {
        private ChangeFlowEntity updatedEntity;
        private boolean importantChangeOccurred;

        public SaveResult(ChangeFlowEntity updatedEntity, boolean importantChangeOccurred) {
            this.updatedEntity = updatedEntity;
            this.importantChangeOccurred = importantChangeOccurred;
        }

        public ChangeFlowEntity getUpdatedEntity() {
            return updatedEntity;
        }

        public boolean isImportantChangeOccurred() {
            return importantChangeOccurred;
        }
    }

    // DTO nội bộ để trả về cả nodes, edges và Map tối ưu
    public static class FlowDataDTO {
        private Map<Long, FlowEdgeModel> indexedEdges;
        private Map<String, FlowNodeModel> indexedNodes;

        public FlowDataDTO(Map<Long, FlowEdgeModel> indexedEdges,
                           Map<String, FlowNodeModel> indexedNodes) {
            this.indexedEdges = indexedEdges;
            this.indexedNodes = indexedNodes;
        }

        // Factory method to build FlowDataDTO from raw lists
        public static FlowDataDTO buildFromLists(List<FlowNodeModel> nodes,
                                                 List<FlowEdgeModel> edges) {
            Map<Long, FlowEdgeModel> indexedEdges = Collections.emptyMap();
            if (edges != null && !edges.isEmpty()) {
                indexedEdges = edges.stream().filter(edge -> edge.getSourceHandle() != null &&
                        edge.getSourceHandle().getChangeStatusId() != null).collect(
                        Collectors.toMap(edge -> edge.getSourceHandle().getChangeStatusId(),
                                edge -> edge, (existing, replacement) -> existing));
            }

            Map<String, FlowNodeModel> indexedNodes = Collections.emptyMap();
            if (nodes != null && !nodes.isEmpty()) {
                indexedNodes = nodes.stream().collect(
                        Collectors.toMap(FlowNodeModel::getId, node -> node,
                                (existing, replacement) -> existing));
            }
            return new FlowDataDTO(indexedEdges, indexedNodes);
        }

        public List<FlowNodeModel> getNodes() {
            return indexedNodes != null ? new ArrayList<>(indexedNodes.values()) :
                    Collections.emptyList();
        }

        public List<FlowEdgeModel> getEdges() {
            return indexedEdges != null ? new ArrayList<>(indexedEdges.values()) :
                    Collections.emptyList();
        }

        public Map<Long, FlowEdgeModel> getIndexedEdges() {
            return indexedEdges;
        }

        public Map<String, FlowNodeModel> getIndexedNodes() {
            return indexedNodes;
        }
    }

}
