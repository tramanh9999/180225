package com.example.demo.model;

import com.example.demo.enums.ChangeFlowNodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing a change flow node.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeFlowNodeModel {
    private Long id;
    private Long changeFlowId;
    private ChangeFlowNodeType type;
    private Integer level;
}
