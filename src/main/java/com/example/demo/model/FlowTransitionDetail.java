package com.example.demo.model;

import com.example.demo.entity.ChangeRequestEntity;
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