package com.example.demo.controller;

import com.example.demo.model.FlowNode;
import com.example.demo.service.FlowService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/flow-nodes")
public class FlowController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private FlowService flowService;

    @GetMapping
    public List<FlowNode> getAllFlowNodes() {
        return flowService.getAllFlowNodes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlowNode> getFlowNodeById(@PathVariable String id) {
        FlowNode flowNode = flowService.getFlowNodeById(id);
        return flowNode != null ? ResponseEntity.ok(flowNode) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public FlowNode createFlowNode(@RequestBody FlowNode flowNode) {
        return flowService.createFlowNode(flowNode);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlowNode> updateFlowNode(@PathVariable String id,
                                                   @RequestBody FlowNode flowNode) {
        FlowNode updatedFlowNode = flowService.updateFlowNode(id, flowNode);
        return updatedFlowNode != null ? ResponseEntity.ok(updatedFlowNode) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlowNode(@PathVariable String id) {
        return flowService.deleteFlowNode(id) ? ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/test-load-flow-data")
    public ResponseEntity<Map<String, Object>> testLoadFlowData() {
        try {
            // Paths to JSON files
            String edgesPath = "src/main/java/com/example/demo/flow/flowEdges.json";
            String nodesPath = "src/main/java/com/example/demo/flow/flowNode.json";

            // Read JSON files
            String edgesJson = new String(Files.readAllBytes(Paths.get(edgesPath)));
            String nodesJson = new String(Files.readAllBytes(Paths.get(nodesPath)));

            // Parse JSON to objects
            List<Map<String, Object>> edges = objectMapper.readValue(edgesJson,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
            List<Map<String, Object>> nodes = objectMapper.readValue(nodesJson,
                    new TypeReference<List<Map<String, Object>>>() {
                    });

            // Log data to console
            System.out.println("=== Flow Edges ===");
            edges.forEach(edge -> System.out.println(edge));

            System.out.println("\n=== Flow Nodes ===");
            nodes.forEach(node -> System.out.println(node));

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("edges", edges);
            response.put("nodes", nodes);
            response.put("edgesCount", edges.size());
            response.put("nodesCount", nodes.size());

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to load flow data", "message", e.getMessage()));
        }
    }

    @GetMapping("/next-nodes/{statusId}")
    public ResponseEntity<Map<String, Object>> findNextNodesFromStatusId(
            @PathVariable Integer statusId) {
        try {
            // Paths to JSON files
            String edgesPath = "src/main/java/com/example/demo/flow/flowEdges.json";
            String nodesPath = "src/main/java/com/example/demo/flow/flowNode.json";

            // Read JSON files
            String edgesJson = new String(Files.readAllBytes(Paths.get(edgesPath)));
            String nodesJson = new String(Files.readAllBytes(Paths.get(nodesPath)));

            // Parse JSON to objects
            List<Map<String, Object>> edges = objectMapper.readValue(edgesJson,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
            List<Map<String, Object>> nodes = objectMapper.readValue(nodesJson,
                    new TypeReference<List<Map<String, Object>>>() {
                    });

            // Find the node containing the statusId
            Map<String, Object> sourceNode = findNodeByStatusId(nodes, statusId);

            if (sourceNode == null) {
                return ResponseEntity.notFound().build();
            }

            // Find the next nodes based on edges
            List<Map<String, Object>> nextNodes =
                    findNextNodes(edges, nodes, (String) sourceNode.get("id"));

            Map<String, Object> response = new HashMap<>();
            response.put("currentNode", sourceNode);
            response.put("nextNodes", nextNodes);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to process flow data", "message",
                            e.getMessage()));
        }
    }

    /**
     * Find a node containing a specific status ID in its statusList
     */
    private Map<String, Object> findNodeByStatusId(List<Map<String, Object>> nodes,
                                                   Integer statusId) {
        for (Map<String, Object> node : nodes) {
            if (node.containsKey("data") &&
                    ((Map<String, Object>) node.get("data")).containsKey("statusList")) {
                List<Map<String, Object>> statusList =
                        (List<Map<String, Object>>) ((Map<String, Object>) node.get("data")).get(
                                "statusList");

                for (Map<String, Object> status : statusList) {
                    if (status.containsKey("id") && statusId.equals(status.get("id"))) {
                        return node;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Find the next nodes connected to a source node
     */
    private List<Map<String, Object>> findNextNodes(List<Map<String, Object>> edges,
                                                    List<Map<String, Object>> nodes,
                                                    String sourceNodeId) {
        List<Map<String, Object>> nextNodes = new ArrayList<>();
        List<String> nextNodeIds = new ArrayList<>();

        // Find all edges where the source is the current node
        for (Map<String, Object> edge : edges) {
            if (sourceNodeId.equals(edge.get("source"))) {
                String targetId = (String) edge.get("target");
                if (!nextNodeIds.contains(targetId)) {
                    nextNodeIds.add(targetId);
                }
            }
        }

        // Find all node objects for the collected IDs
        for (String nextNodeId : nextNodeIds) {
            for (Map<String, Object> node : nodes) {
                if (nextNodeId.equals(node.get("id"))) {
                    nextNodes.add(node);
                    break;
                }
            }
        }

        return nextNodes;
    }
}
