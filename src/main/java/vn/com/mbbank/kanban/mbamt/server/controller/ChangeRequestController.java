package vn.com.mbbank.kanban.mbamt.server.controller;

import vn.com.mbbank.kanban.mbamt.server.constants.ServerUrl;
import com.example.demo.model.*;
import vn.com.mbbank.kanban.mbamt.server.model.*;
import vn.com.mbbank.kanban.mbamt.server.demo.model.*;
import vn.com.mbbank.kanban.mbamt.server.repository.ChangeRequestService;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeRequestRoleService;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeStatusService;
import vn.com.mbbank.kanban.mbamt.server.service.FlowManagementService;
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
    private ChangeStatusService changeStatusService;

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

//    @GetMapping("/{id}/change-roles")
//    public ResponseEntity<List<ChangeRequestRoleModel>> findCurrentAndNextNodeByCurrentPoint(
//            @PathVariable("id") Long id) {
//        var res = changeRequestRoleService.findAllByChangeRequestId(id);
//        return ResponseEntity.ok(res);
//    }


    /**
     * Allows Change Coordinator to reply to an approval request for a Change Request.
     *
     * @param replyModel The model containing the reply details.
     * @return The updated ChangeRequestApprovalResultModel after processing the reply.
     */
    @PostMapping("/approval-requests/reply")
    public ResponseEntity<ChangeRequestModel> replyToApprovalRequest(
            @RequestBody ChangeRequestApprovalResultModel replyModel) {
        ChangeRequestModel updatedApprovalRequest =
                changeRequestService.processChangeRequestApprovalReply(replyModel);
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
    @PostMapping("/{changeRequestId}/process-change-request")
    public ResponseEntity<ChangeRequestModel> transitionByChangeStatus(
            @PathVariable Long changeRequestId,
            @RequestBody ChangeProcessModel changeProcessModel) {
        ChangeRequestModel updatedCr =
                changeRequestService.processChangeRequestCoordinatorTransition(changeRequestId,
                        changeProcessModel);
        return ResponseEntity.ok(updatedCr);
    }

    // get all change statuses for a change request  with path variable  is change request id
    @GetMapping("/{changeRequestId}/statuses")
    public ResponseEntity<List<ChangeStatusModel>> getAllChangeStatusesForChangeRequest(
            @PathVariable Long changeRequestId) {
        List<ChangeStatusModel> remainingStatusesInSameStage =
                changeStatusService.findAllChangeStatusInSameStage(changeRequestId);
        return ResponseEntity.ok(remainingStatusesInSameStage);
    }

    // api for getChangeRequestRoleByChangeFlowNodeId
    @GetMapping("/{changeRequestId}/changeTemplate/{changeTemplateId}" +
            "/change-flow-node/{changeFlowNodeId}")
    public ResponseEntity<ChangeRequestRoleModel> getChangeRequestRoleByChangeFlowNodeId(
            @PathVariable Long changeRequestId, @PathVariable Long changeTemplateId,
            @PathVariable Long changeFlowNodeId) {
        ChangeRequestRoleModel changeRequestRole =
                changeRequestRoleService.getChangeRequestRoleByChangeFlowNodeId(changeRequestId,
                        changeTemplateId, changeFlowNodeId);
        return ResponseEntity.ok(changeRequestRole);
    }

}