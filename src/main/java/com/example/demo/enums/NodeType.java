package com.example.demo.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

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


}