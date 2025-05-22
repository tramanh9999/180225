package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

import com.example.demo.model.ChangeTemplateRoleModel;
import com.example.demo.model.LevelGroupModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeTemplateModel {

    private Long id;
    private String name;
    private String description;
    private List<LevelGroupModel> levels;

}
