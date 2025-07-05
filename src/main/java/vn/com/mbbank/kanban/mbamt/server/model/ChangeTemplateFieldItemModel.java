package vn.com.mbbank.kanban.mbamt.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeTemplateFieldItemModel {
    private Long id;
    private Long changeTemplateId;
    private Long customFieldId;
    private String fieldValue;
}
