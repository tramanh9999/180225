package com.example.demo.enums;

/**
 * Enum representing the types of nodes in the workflow flow.
 * Extracted from the flowEdges.json file.
 */
public enum FlowNodeType {
    START("start"), END("end"), SUBMISSION_PLANNING("SUBMISSION_PLANNING"), APPROVAL("APPROVAL"),
    REVIEW_CLOSE("REVIEW_CLOSE"), APPROVAL_NODE("APPROVAL_NODE"), IMPLEMENTATION("IMPLEMENTATION"),
    CAB_NODE("CAB_NODE");

    private final String value;

    FlowNodeType(String value) {
        this.value = value;
    }

    /**
     * Find a FlowNodeType by its prefix in a node ID
     *
     * @param nodeId The full node ID (e.g., "APPROVAL-1750143826680")
     * @return The matching FlowNodeType or null if no match found
     */
    public static FlowNodeType fromNodeId(String nodeId) {
        if (nodeId == null || nodeId.isEmpty()) {
            return null;
        }

        if (nodeId.equals("start")) {
            return START;
        }

        if (nodeId.equals("end")) {
            return END;
        }

        // For other nodes, split by dash and get the type part
        String[] parts = nodeId.split("-");
        if (parts.length > 0) {
            String typePrefix = parts[0];
            for (FlowNodeType type : values()) {
                if (type.getValue().equals(typePrefix)) {
                    return type;
                }
            }
        }

        return null;
    }

    public String getValue() {
        return value;
    }
}
