package com.example.demo.model;

import lombok.Builder;

@Builder
public record FlowNavigationInfo(FlowEdgeModel currentEdge, FlowNodeModel targetNode) {

}

