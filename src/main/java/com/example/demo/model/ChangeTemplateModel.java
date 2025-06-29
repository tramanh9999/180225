package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeTemplateModel {

    private Long id;
    private String name;
    private String description;
    private List<LevelGroupModel> levels;

    private Long changeFlowId;

}
