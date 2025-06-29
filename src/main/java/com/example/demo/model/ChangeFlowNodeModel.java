package com.example.demo.model;

import com.example.demo.enums.ChangeFlowNodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Model representing a change flow node.
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ChangeFlowNodeModel {
    private Long id;
    private Long changeFlowId;
    private ChangeFlowNodeType type;
    private Integer nodeLevel;
    private String name;


}
