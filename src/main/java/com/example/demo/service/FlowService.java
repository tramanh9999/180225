package com.example.demo.service;

import java.util.List;

import com.example.demo.model.FlowNode;

public interface FlowService {
    List<FlowNode> getAllFlowNodes();

    FlowNode getFlowNodeById(String id);

    FlowNode createFlowNode(FlowNode flowNode);

    FlowNode updateFlowNode(String id, FlowNode flowNode);

    boolean deleteFlowNode(String id);
}
