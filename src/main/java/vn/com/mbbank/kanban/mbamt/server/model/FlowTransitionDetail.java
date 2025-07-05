package vn.com.mbbank.kanban.mbamt.server.model;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlowTransitionDetail {

    public ChangeRequestRoleModel changeRole;
    IndexedChangeFlowDataModel flowData;
    private Long changeFlowId;
    private ChangeRequestEntity changeRequest;
    //current point of change request in change flow
    private String currentChangeFlowNodeHandleId;
    private ChangeRequestApprovalModel changeRequestApproval;
}