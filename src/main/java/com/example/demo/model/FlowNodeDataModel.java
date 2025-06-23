package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowNodeDataModel {
    // Các trường chung cho hầu hết các node
    private String label;

    // Các trường cho node loại "stage" (SUBMISSION_PLANNING, APPROVAL, REVIEW_CLOSE, IMPLEMENTATION)
    private String stageType;
    private List<ChangeStatusModel> statusList;

    // Các trường cho node loại "process" (APPROVAL_NODE, CAB_NODE)
    // Lưu ý: id, nodeId ở đây là trùng lặp với FlowNodeDTO.id. Có thể bỏ qua khi tạo DTO.
    // private String id; // Trùng lặp với FlowNodeDTO.id
    // private String nodeId; // Trùng lặp với FlowNodeDTO.id
    private String name;
    private String type; // Kiểu nội bộ của process node (e.g., "APPROVAL", "CAB")
    private List<SysGroupModel> groups;
    private Integer nodeLevel;

    // TODO: Nếu có các loại node khác với các trường dữ liệu riêng, hãy thêm chúng vào đây.
    // Hoặc nếu cấu trúc quá khác biệt, cân nhắc sử dụng @JsonTypeInfo cho đa hình JSON.
}
