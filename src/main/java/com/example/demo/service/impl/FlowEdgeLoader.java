package com.example.demo.service.impl;

import com.example.demo.model.FlowEdge;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FlowEdgeLoader {

    /**
     * Loads flow edges from JSON string
     *
     * @param jsonString JSON string containing flow edge data
     * @return List of FlowEdge objects
     * @throws IOException if parsing fails
     */
    public static List<FlowEdge> loadFromJsonString(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, new TypeReference<List<FlowEdge>>() {
        });
    }

    /**
     * Loads flow edges from a JSON file
     *
     * @param filePath path to the JSON file
     * @return List of FlowEdge objects
     * @throws IOException if file reading or parsing fails
     */
    public static List<FlowEdge> loadFromFile(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return loadFromJsonString(content);
    }

    /**
     * Example usage
     */
    public static void main(String[] args) {
        try {
            // Example loading from file
            String filePath =
                    "/Users/anhhtjse/Fullstack/demo/src/main/java/com/example/demo/flow/flowEdges.json";
            List<FlowEdge> edges = loadFromFile(filePath);
            System.out.println("Loaded " + edges.size() + " flow edges from file");

            // Print first edge as example
            if (!edges.isEmpty()) {
                System.out.println("Example edge: " + edges.get(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
