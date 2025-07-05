package vn.com.mbbank.kanban.mbamt.server.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CHANGE_WORK_FLOW_NODE")
@Data
public class ChangeNodeEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NODE_NAME")
    private String nodeName;
}