package com.example.demo.model;

import com.example.demo.enums.ChangeStage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model representing the data of a flow node.
 * This includes the label, stage type, status list, name, type, groups, and node level.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowNodeDataModel {
    private String label;
    private ChangeStage stageType;
    private List<ChangeStatusModel> statusList;
    private String name;
    private String type;
    private List<SysGroupModel> groups;
    private Integer nodeLevel;

}
