package com.example.demo.model;

import com.example.demo.enums.NodeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowNodeModel {
    private String id;
    private NodeType type; // Sử dụng Enum cho loại node để có ngữ nghĩa hơn
    private FlowNodeDataModel data; // Sử dụng DTO chi tiết cho trường data
    private boolean deletable;
}
