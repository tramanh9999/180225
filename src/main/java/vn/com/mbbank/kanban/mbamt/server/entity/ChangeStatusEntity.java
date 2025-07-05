package vn.com.mbbank.kanban.mbamt.server.entity;

import vn.com.mbbank.kanban.mbamt.server.enums.ChangeStage;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "CHANGE_STATUS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Ensures builder inheritance if BaseEntity uses it
public class ChangeStatusEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @Column(name = "ACTION", length = 100)
    private String action;


    @Enumerated(EnumType.STRING)
    @Column(name = "STAGE")
    private ChangeStage stage;


}