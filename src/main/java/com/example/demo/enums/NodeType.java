package com.example.demo.enums;


import lombok.Getter;

/**
 * Enum representing different types of nodes in a workflow.
 * Each node type has a prefix that can be used to identify it from a node ID.
 */
@Getter
public enum NodeType {
    START("start"), END("end"), SUBMISSION_PLANNING("SUBMISSION_PLANNING"), APPROVAL("APPROVAL"),
    REVIEW_CLOSE("REVIEW_CLOSE"), IMPLEMENTATION("IMPLEMENTATION"), APPROVAL_NODE("APPROVAL_NODE"),
    CAB_NODE("CAB_NODE"), UNKNOWN("unknown");

    private final String prefix;

    NodeType(String prefix) {
        this.prefix = prefix;
    }
}