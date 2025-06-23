package com.example.demo.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Collections;
import java.util.List;

public class FlowDataParser {

    // Không cần ObjectMapper nữa khi sử dụng Fastjson

    /**
     * Chuyển đổi chuỗi JSON (danh sách các flowNode) sang List<FlowNodeDTO>.
     *
     * @param jsonString Chuỗi JSON chứa mảng các đối tượng flowNode.
     * @return Danh sách các FlowNodeDTO, hoặc danh sách rỗng nếu có lỗi trong quá trình phân tích cú pháp.
     */
    public static List<FlowNodeModel> parseFlowNodesFromJson(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return Collections.emptyList();
        }

        try {
            // Sử dụng JSON.parseArray để phân tích cú pháp mảng JSON
            // TypeReference là cần thiết để Fastjson biết kiểu của các phần tử trong danh sách
            return JSON.parseObject(jsonString, new TypeReference<List<FlowNodeModel>>() {
            });
        } catch (Exception e) { // Fastjson sử dụng generic Exception cho lỗi parsing
            System.err.println("Error parsing JSON string to List<FlowNodeDTO> using Fastjson: " +
                    e.getMessage());
            e.printStackTrace(); // In stack trace để debug
            return Collections.emptyList(); // Trả về danh sách rỗng khi có lỗi
        }
    }

    /**
     * Chuyển đổi một FlowNodeDTO thành chuỗi JSON.
     * Đây là phương thức hữu ích để kiểm tra hoặc gửi dữ liệu đi.
     *
     * @param nodeDTO Đối tượng FlowNodeDTO cần chuyển đổi.
     * @return Chuỗi JSON biểu diễn FlowNodeDTO.
     */
    public static String convertFlowNodeDtoToJson(FlowNodeModel nodeDTO) {
        try {
            // Sử dụng JSON.toJSONString để chuyển đổi đối tượng thành chuỗi JSON
            // PrettyFormat để định dạng JSON đẹp
            return JSON.toJSONString(nodeDTO, SerializerFeature.PrettyFormat);
        } catch (Exception e) {
            System.err.println("Error converting FlowNodeDTO to JSON string using Fastjson: " +
                    e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Chuyển đổi danh sách FlowNodeDTO thành chuỗi JSON (dạng mảng).
     *
     * @param nodeDTOs Danh sách các FlowNodeDTO cần chuyển đổi.
     * @return Chuỗi JSON biểu diễn danh sách FlowNodeDTO dưới dạng mảng JSON.
     */
    public static String convertFlowNodesDtoListToJson(List<FlowNodeModel> nodeDTOs) {
        try {
            // Sử dụng JSON.toJSONString để chuyển đổi danh sách thành chuỗi JSON
            // PrettyFormat để định dạng JSON đẹp
            return JSON.toJSONString(nodeDTOs, SerializerFeature.PrettyFormat);
        } catch (Exception e) {
            System.err.println(
                    "Error converting List<FlowNodeDTO> to JSON string using Fastjson: " +
                            e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Chuyển đổi chuỗi JSON (danh sách các flowEdge) sang List<FlowEdgeDTO>.
     *
     * @param jsonString Chuỗi JSON chứa mảng các đối tượng flowEdge.
     * @return Danh sách các FlowEdgeDTO, hoặc danh sách rỗng nếu có lỗi trong quá trình phân tích cú pháp.
     */
    public static List<FlowEdgeModel> parseFlowEdgesFromJson(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return Collections.emptyList();
        }

        try {
            // Sử dụng JSON.parseObject để phân tích cú pháp mảng JSON
            // TypeReference là cần thiết để Fastjson biết kiểu của các phần tử trong danh sách
            List<FlowEdge> flowEdges =
                    JSON.parseObject(jsonString, new TypeReference<List<FlowEdge>>() {
                    });
            return flowEdges.stream()
                    .map(FlowEdgeModel::fromSimpleFlowEdge) // Chuyển đổi từng FlowEdge
                    // sang
                    // FlowEdgeModel
                    .toList(); // Trả về danh sách FlowEdgeModel
        } catch (Exception e) {
            System.err.println("Error parsing JSON string to List<FlowEdgeDTO> using Fastjson: " +
                    e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Chuyển đổi một FlowEdgeDTO thành chuỗi JSON.
     *
     * @param edgeDTO Đối tượng FlowEdgeDTO cần chuyển đổi.
     * @return Chuỗi JSON biểu diễn FlowEdgeDTO.
     */
    public static String convertFlowEdgeDtoToJson(FlowEdgeModel edgeDTO) {
        try {
            return JSON.toJSONString(edgeDTO, SerializerFeature.PrettyFormat);
        } catch (Exception e) {
            System.err.println("Error converting FlowEdgeDTO to JSON string using Fastjson: " +
                    e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Chuyển đổi danh sách FlowEdgeDTO thành chuỗi JSON (dạng mảng).
     *
     * @param edgeDTOs Danh sách các FlowEdgeDTO cần chuyển đổi.
     * @return Chuỗi JSON biểu diễn danh sách FlowEdgeDTO dưới dạng mảng JSON.
     */
    public static String convertFlowEdgesDtoListToJson(List<FlowEdgeModel> edgeDTOs) {
        try {
            return JSON.toJSONString(edgeDTOs, SerializerFeature.PrettyFormat);
        } catch (Exception e) {
            System.err.println(
                    "Error converting List<FlowEdgeDTO> to JSON string using Fastjson: " +
                            e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
