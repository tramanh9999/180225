package vn.com.mbbank.kanban.mbamt.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.mbbank.kanban.mbamt.server.enums.ChangeFlowNodeTypeEnum;

@Entity
@Table(name = "CHANGE_FLOW_NODE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeFlowNodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "change_flow_node_seq")
    @SequenceGenerator(name = "change_flow_node_seq", sequenceName = "CHANGE_FLOW_NODE_SEQ",
            allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "CHANGE_FLOW_ID")
    private Long changeFlowId;

    @Column(name = "TYPE", length = 255)
    @Enumerated(EnumType.STRING)
    private ChangeFlowNodeTypeEnum type;

    @Column(name = "NAME", length = 255)
    private String name;

    @Column(name = "NODE_LEVEL")
    private Integer nodeLevel;

    @Column(name = "NODE_ID", length = 255)
    private String nodeId;
}
