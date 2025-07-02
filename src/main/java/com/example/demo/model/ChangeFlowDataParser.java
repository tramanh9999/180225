package com.example.demo.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utility class for parsing and converting flow node and edge data to and from JSON.
 */
@Slf4j
public class ChangeFlowDataParser {


    private static final String KEY_DELIMITER = "::";

    /**
     * Parse a JSON string into a list of FlowNodeModel objects.
     *
     * @param jsonString the JSON string to parse
     * @return a list of FlowNodeModel objects, or an empty list if parsing fails
     */
    public static List<FlowNodeModel> parseFlowNodesFromJson(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return JSON.parseObject(jsonString, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("Error parsing JSON string to List<FlowNodeModel> using Fastjson: {}",
                    e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Convert a list of FlowNodeModel objects to a JSON string.
     *
     * @param nodeModels the list of FlowNodeModel objects
     * @return the JSON string representation, or null if conversion fails
     */
    public static String convertFlowNodesModelListToJson(List<FlowNodeModel> nodeModels) {
        try {
            return JSON.toJSONString(nodeModels, SerializerFeature.PrettyFormat);
        } catch (Exception e) {
            log.error("Error converting List<FlowNodeModel> to JSON string using Fastjson: {}",
                    e.getMessage(), e);
            return null;
        }
    }

    /**
     * Parse a JSON string into a list of FlowEdgeModel objects.
     *
     * @param jsonString   the JSON string to parse
     * @param indexedNodes
     * @return a list of FlowEdgeModel objects, or an empty list if parsing fails
     */
    public static List<FlowEdgeModel> parseFlowEdgesFromJson(String jsonString,
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
                    .map(rawEdge -> FlowEdgeModel.fromSimpleFlowEdge(rawEdge, indexedNodes))
                    .toList();
        } catch (Exception e) {
            log.error("Error parsing JSON string to List<FlowEdgeModel> using Fastjson: {}",
                    e.getMessage(), e);
            return Collections.emptyList();
        }
    }

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
     * Split a mapKey into the source node ID and source handle ID.
     *
     * @param mapKey The mapKey string to split.
     * @return A String array containing [sourceNodeId, sourceHandleId].
     * @throws IllegalArgumentException if mapKey is invalid or cannot be split.
     */
    public static String[] parseMapKey(String mapKey) {
        if (mapKey == null || mapKey.isEmpty()) {
            throw new IllegalArgumentException("Map key cannot be null or empty.");
        }

        // Check if mapKey contains the delimiter
        if (!mapKey.contains(KEY_DELIMITER)) {
            throw new IllegalArgumentException(
                    "Invalid map key format. Missing delimiter '" + KEY_DELIMITER + "'.");
        }

        // Split the string
        String[] parts = mapKey.split(Pattern.quote(KEY_DELIMITER),
                2); // Limit 2 to ensure only split at the first delimiter

        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Invalid map key format. Expected 2 parts, but found " + parts.length +
                            " in '" + mapKey + "'.");
        }

        return parts;
    }

    /**
     * Get the source node ID from a mapKey.
     *
     * @param mapKey The mapKey string.
     * @return The source node ID.
     */
    public static String getSourceNodeIdFromMapKey(String mapKey) {
        return parseMapKey(mapKey)[0];
    }

    /**
     * Get the source handle ID from a mapKey.
     *
     * @param mapKey The mapKey string.
     * @return The source handle ID.
     */
    public static String getSourceHandleIdFromMapKey(String mapKey) {
        return parseMapKey(mapKey)[1];
    }


}