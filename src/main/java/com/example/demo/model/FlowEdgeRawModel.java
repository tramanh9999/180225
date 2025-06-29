package com.example.demo.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Represents an edge in a flow diagram.
 * This class is used to define the connections between nodes in a flow.
 */
@Data
@RequiredArgsConstructor
public class FlowEdgeRawModel {
    private String id;
    private String source;
    private String target;
    private String sourceHandle;
    private String targetHandle;
}
