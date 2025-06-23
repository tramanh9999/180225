package com.example.demo.model;

/**
 * Enum representing the types of edges (transitions) in the workflow flow.
 * Extracted from the sourceHandle and targetHandle patterns in flowEdges.json.
 */
public enum FlowEdgeType {
    DEFAULT("default"), INPUT("input"), OUTPUT("output"), ACCEPT("Accept"), REJECT("Reject");

    private final String value;

    FlowEdgeType(String value) {
        this.value = value;
    }

    /**
     * Extract edge type from a handle identifier
     *
     * @param handle The handle identifier (e.g.,
     *               "APPROVAL_NODE-1750059646263-Accept-output")
     * @return The matching FlowEdgeType or DEFAULT if no specific type found
     */
    public static FlowEdgeType fromHandleId(String handle) {
        if (handle == null || handle.isEmpty()) {
            return DEFAULT;
        }

        if (handle.contains("-input")) {
            return INPUT;
        }

        if (handle.contains("-output")) {
            return OUTPUT;
        }

        if (handle.contains("-Accept-")) {
            return ACCEPT;
        }

        if (handle.contains("-Reject-")) {
            return REJECT;
        }

        return DEFAULT;
    }

    public String getValue() {
        return value;
    }
}
