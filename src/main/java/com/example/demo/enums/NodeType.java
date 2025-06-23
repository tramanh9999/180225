package com.example.demo.enums;

public enum NodeType {
    START("start"), END("end"), SUBMISSION_PLANNING("SUBMISSION_PLANNING"),
    APPROVAL("APPROVAL"), // Node phê duyệt chung
    APPROVAL_NODE("APPROVAL_NODE"), // Node phê duyệt cụ thể
    REVIEW_CLOSE("REVIEW_CLOSE"), IMPLEMENTATION("IMPLEMENTATION"),
    CAB_NODE("CAB_NODE"), // Node Hội đồng tư vấn thay đổi
    UNKNOWN("unknown"); // Cho các loại node không xác định

    private final String prefix;

    NodeType(String prefix) {
        this.prefix = prefix;
    }

    // Phương thức tiện ích để chuyển đổi từ String (Node ID) sang NodeType
    public static NodeType fromNodeId(String nodeId) {
        if (nodeId == null || nodeId.isEmpty()) {
            return UNKNOWN;
        }
        for (NodeType type : NodeType.values()) {
            if (type.getPrefix() != null && nodeId.startsWith(type.getPrefix())) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public String getPrefix() {
        return prefix;
    }
}
