package vn.com.mbbank.kanban.mbamt.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomFieldValueModel {
    private Long id;
    private Long customFieldId;
    private String fieldValue;
    private Long min;
    private Long max;
    private Boolean isDefault;
}
