package vn.com.mbbank.kanban.mbamt.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity for CUSTOM_FIELD_PICKLIST table.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CUSTOM_FIELD_PICKLIST")
public class CustomFieldPicklistEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CUSTOM_FIELD_ID")
    private Long customFieldId;

    @Column(name = "IS_DEFAULT")
    private Boolean isDefault;

    @Column(name = "PICKLIST_VALUE")
    private String picklistValue;

    @Column(name = "POSTITION")
    private Integer position;
}
