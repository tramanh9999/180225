package com.example.demo.model;

import com.example.demo.entity.ChangeFlowEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Represents the result of a save operation in the flow management system.
 * This class encapsulates the updated entity and a flag indicating whether
 * an important change occurred during the save operation.
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class SaveResult {
    private ChangeFlowEntity updatedEntity;
    private boolean importantChangeOccurred;
}