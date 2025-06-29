package com.example.demo.controller;

import com.example.demo.constants.ServerUrl;
import com.example.demo.model.ChangeRequestApprovalResultModel;
import com.example.demo.model.ChangeRequestModel;
import com.example.demo.model.ChangeRequestRoleModel;
import com.example.demo.model.FlowEdgeModel;
import com.example.demo.repository.ChangeRequestService;
import com.example.demo.service.ChangeRequestRoleService;
import com.example.demo.service.FlowManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Change Request Roles.
 */
@RestController
@RequestMapping(ServerUrl.CHANGE_REQUEST)
@RequiredArgsConstructor
public class ChangeRequestController {

    private final ChangeRequestRoleService changeRequestRoleService;
    private final FlowManagementService flowManagementService;
    private final ChangeRequestService changeRequestService;

    @GetMapping("/change-flow/{changeFlowId}/change-flow-node/{changeFlowNodeId}")
    public ResponseEntity<FlowEdgeModel> findCurrentAndNextNodeByCurrentPoint(
            @PathVariable("changeFlowId") Long changeFlowId,
            @RequestParam(value = "handleId", required = false) String handleId) {
//        var flowNodeModel =
//                flowManagementService.findCurrentAndNextNodeBySourceHandleId(changeFlowId,
//                        handleId);
//        return ResponseEntity.ok(flowNodeModel);

        return null;
    }

    @GetMapping("/{id}/change-roles")
    public ResponseEntity<List<ChangeRequestRoleModel>> findCurrentAndNextNodeByCurrentPoint(
            @PathVariable("id") Long id) {
        var res = changeRequestRoleService.findAllByChangeRequestId(id);
        return ResponseEntity.ok(res);
    }


    /**
     * Allows Change Coordinator to reply to an approval request for a Change Request.
     *
     * @param replyModel The model containing the reply details.
     * @return The updated ChangeRequestApprovalResultModel after processing the reply.
     */
    @PostMapping("/approval-requests/reply")
    public ResponseEntity<ChangeRequestApprovalResultModel> replyToApprovalRequest(
            @RequestBody ChangeRequestApprovalResultModel replyModel) {
        ChangeRequestApprovalResultModel updatedApprovalRequest =
                changeRequestService.processApprovalReply(replyModel);
        return ResponseEntity.ok(updatedApprovalRequest);
    }


    /**
     * Allows Change Coordinator to explicitly transition the status of a Change Request
     * by specifying the outgoing handle from its current workflow node.
     * This will typically lead to a new stage node.
     *
     * @param changeRequestId The ID of the Change Request to transition.
     * @return The updated ChangeRequestModel.
     */
    @PostMapping("/{changeRequestId}/transition-status")
    public ResponseEntity<ChangeRequestModel> transitionChangeRequestStatus(
            @PathVariable Long changeRequestId, Long changeStatusId) {
        ChangeRequestModel updatedCr =
                changeRequestService.processChangeRequestCoordinatorTransition(changeRequestId,
                        changeStatusId);
        return ResponseEntity.ok(updatedCr);
    }
}