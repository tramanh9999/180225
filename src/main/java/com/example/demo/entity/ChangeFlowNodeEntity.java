package com.example.demo.entity;

import com.example.demo.enums.ChangeFlowNodeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private ChangeFlowNodeType type;

    @Column(name = "NAME", length = 255)
    private String name;

    @Column(name = "NODE_LEVEL")
    private Integer nodeLevel;

    @Column(name = "NODE_ID", length = 255)
    private String nodeId;
}
