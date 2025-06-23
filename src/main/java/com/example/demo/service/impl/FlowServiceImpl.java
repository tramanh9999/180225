package com.example.demo.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.service.FlowService;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.example.demo.model.FlowNode;

import jakarta.annotation.PostConstruct;

@Service
public class FlowServiceImpl implements FlowService {

    private final String FILE_PATH = "src/main/java/com/example/demo/flow/flowNode.json";
    private List<FlowNode> flowNodes = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();
                String jsonStr = new String(data, StandardCharsets.UTF_8);
                flowNodes = JSON.parseArray(jsonStr, FlowNode.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read flow nodes from a JSON string
     *
     * @param jsonContent JSON string containing flow nodes
     * @return List of FlowNode objects
     */
    public List<FlowNode> readFlowNodesFromString(String jsonContent) {
        try {
            return JSON.parseArray(jsonContent, FlowNode.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<FlowNode> getAllFlowNodes() {
        return flowNodes;
    }

    @Override
    public FlowNode getFlowNodeById(String id) {
        return flowNodes.stream().filter(node -> node.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public FlowNode createFlowNode(FlowNode flowNode) {
        flowNodes.add(flowNode);
        saveToFile();
        return flowNode;
    }

    @Override
    public FlowNode updateFlowNode(String id, FlowNode updatedNode) {
        FlowNode existingNode = getFlowNodeById(id);
        if (existingNode != null) {
            updatedNode.setId(id);
            flowNodes = flowNodes.stream().filter(node -> !node.getId().equals(id))
                    .collect(Collectors.toList());
            flowNodes.add(updatedNode);
            saveToFile();
            return updatedNode;
        }
        return null;
    }

    @Override
    public boolean deleteFlowNode(String id) {
        int initialSize = flowNodes.size();
        flowNodes = flowNodes.stream().filter(node -> !node.getId().equals(id))
                .collect(Collectors.toList());
        if (flowNodes.size() < initialSize) {
            saveToFile();
            return true;
        }
        return false;
    }

    private void saveToFile() {
        try {
            String jsonString = JSON.toJSONString(flowNodes, true);
            FileWriter fileWriter = new FileWriter(FILE_PATH);
            fileWriter.write(jsonString);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
