package vn.com.mbbank.kanban.mbamt.server.entity;

import vn.com.mbbank.kanban.mbamt.server.enums.ApprovalResultStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "CHANGE_REQUEST_APPROVAL")
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ChangeRequestApprovalEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CHANGE_REQUEST_ID")
    private Long changeRequestId;

    @Column(name = "CHANGE_REQUEST_ROLE_USER_ID")
    private Long changeRequestRoleUserId;

    @Column(name = "OVERALL_STATUS")
    @Enumerated(EnumType.STRING)
    private ApprovalResultStatus overallStatus;

    @Column(name = "OVERALL_USERNAME")
    private String overallUsername;

}
