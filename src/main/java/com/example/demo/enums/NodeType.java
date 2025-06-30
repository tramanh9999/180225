package com.example.demo.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;

import static com.example.demo.enums.FlowConstants.NODE_APPROVAL_HANDLE_PATTERN;

/**
 * The enum Node type.
 */
@Getter
@RequiredArgsConstructor
public enum NodeType {
    /**
     * Represents the start node / end node in the change flow.
     */
    START(NodeCommonType.START, "start"), END(NodeCommonType.END, "end"),

    /**
     * Represents a stage node in the change flow.
     */
    SUBMISSION_PLANNING(NodeCommonType.CHANGE_STAGE, ChangeStage.SUBMISSION_PLANNING.name()),
    APPROVAL(NodeCommonType.CHANGE_STAGE, ChangeStage.APPROVAL.name()),
    REVIEW_CLOSE(NodeCommonType.CHANGE_STAGE, ChangeStage.REVIEW_CLOSE.name()),
    IMPLEMENTATION(NodeCommonType.CHANGE_STAGE, ChangeStage.IMPLEMENTATION.name()),

    /**
     * Represents a node that handles approval actions in the change flow.
     */
    APPROVAL_NODE(NodeCommonType.CHANGE_ROLE, "APPROVAL_NODE"),
    CAB_NODE(NodeCommonType.CHANGE_ROLE, "CAB_NODE"),
    /**
     * Represents a node that is not recognized or does not fit into any specific category.
     */
    UNKNOWN(NodeCommonType.UNKNOWN, "unknown");


    private final NodeCommonType commonType;
    private final String value;


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
     * Attempts to determine the NodeType based on a given node *handle ID* string.
     * This is primarily possible for custom handle IDs that embed the node's ID.
     *
     * @param nodeHandleId The ID string of the handle (e.g., "APPROVAL_NODE-ABC-Accept-output", "0-output").
     * @return The corresponding NodeType if the node ID can be extracted and matched,
     * otherwise NodeType.UNKNOWN if it's a generic handle or doesn't match a pattern.
     */
    public static NodeType parseTypeFromNodeHandleId(String nodeHandleId) {
        if (nodeHandleId == null || nodeHandleId.isEmpty()) {
            return UNKNOWN;
        }

        Matcher matcher = NODE_APPROVAL_HANDLE_PATTERN.matcher(nodeHandleId);
        if (matcher.matches()) {
            String nodeIdPart = matcher.group(1);
            return parseTypeFromNodeId(nodeIdPart);
        }
        if (nodeHandleId.startsWith(START.getValue())) {
            return START;
        }
        if (nodeHandleId.startsWith(END.getValue())) {
            return END;
        }
        return UNKNOWN;
    }

}