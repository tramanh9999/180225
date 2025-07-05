package vn.com.mbbank.kanban.mbamt.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "CHANGE_FLOW")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeFlowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "change_flow_seq")
    @SequenceGenerator(name = "change_flow_seq", sequenceName = "CHANGE_FLOW_SEQ",
            allocationSize = 1)
    private Long id;

    @Column(name = "NAME", length = 255)
    private String name;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @Lob
    @Column(name = "FLOW_DATA")
    private String flowData;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;

    @Column(name = "MODIFIED_BY", length = 100)
    private String modifiedBy;

    @Column(name = "DELETED", columnDefinition = "NUMBER(1) default 0")
    private Integer deleted;

    @Column(name = "DELETED_BY", length = 100)
    private String deletedBy;

    @Lob
    @Column(name = "FLOW_NODES")
    private String flowNodes;

    @Lob
    @Column(name = "FLOW_EDGES")
    private String flowEdges;

    @Column(name = "DELETED_DATE")
    private LocalDateTime deletedDate;

    @Column(name = "NODE_LEVEL")
    private Long nodeLevel;
}
