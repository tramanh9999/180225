package com.example.demo.model;

import com.example.demo.enums.HandleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing a parsed handle in a workflow system.
 * It includes the raw handle ID, type, change status ID, and custom action.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedHandleModel {
    private String rawHandleId;
    private HandleType type;
    private Long changeStatusId;
    private String customAction;
}
