package com.example.demo.model;

import java.time.LocalDateTime;

import com.example.demo.entity.ChangeFlowEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ChangeFlowModel {

    private Long id;
    private String name;
    private String description;
    private String flowData;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime modifiedDate;
    private String modifiedBy;
    private Integer deleted;
    private String deletedBy;
    private String flowNodes;
    private String flowEdges;
    private LocalDateTime deletedDate;

    // Add convenience methods to convert between entity and DTO
    public static ChangeFlowModel fromEntity(ChangeFlowEntity entity) {
        return ChangeFlowModel.builder().id(entity.getId()).name(entity.getName())
                .description(entity.getDescription()).flowData(entity.getFlowData())
                .createdDate(entity.getCreatedDate()).createdBy(entity.getCreatedBy())
                .modifiedDate(entity.getModifiedDate()).modifiedBy(entity.getModifiedBy())
                .deleted(entity.getDeleted()).deletedBy(entity.getDeletedBy())
                .flowNodes(entity.getFlowNodes()).flowEdges(entity.getFlowEdges())
                .deletedDate(entity.getDeletedDate()).build();
    }

    public ChangeFlowEntity toEntity() {
        ChangeFlowEntity entity = new ChangeFlowEntity();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setDescription(this.description);
        entity.setFlowData(this.flowData);
        entity.setCreatedDate(this.createdDate);
        entity.setCreatedBy(this.createdBy);
        entity.setModifiedDate(this.modifiedDate);
        entity.setModifiedBy(this.modifiedBy);
        entity.setDeleted(this.deleted);
        entity.setDeletedBy(this.deletedBy);
        entity.setFlowNodes(this.flowNodes);
        entity.setFlowEdges(this.flowEdges);
        entity.setDeletedDate(this.deletedDate);
        return entity;
    }
}
