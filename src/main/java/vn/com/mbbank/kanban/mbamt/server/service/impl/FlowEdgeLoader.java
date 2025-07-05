package vn.com.mbbank.kanban.mbamt.server.service.impl;

import vn.com.mbbank.kanban.mbamt.server.model.FlowEdgeRawModel;
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
    public static List<FlowEdgeRawModel> loadFromJsonString(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, new TypeReference<List<FlowEdgeRawModel>>() {
        });
    }

    /**
     * Loads flow edges from a JSON file
     *
     * @param filePath path to the JSON file
     * @return List of FlowEdge objects
     * @throws IOException if file reading or parsing fails
     */
    public static List<FlowEdgeRawModel> loadFromFile(String filePath) throws IOException {
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
            List<FlowEdgeRawModel> edges = loadFromFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
