package com.example.demo.controller;

import com.example.demo.constants.ServerUrl;
import com.example.demo.model.ChangeRequestRoleModel;
import com.example.demo.model.FlowNavigationInfo;
import com.example.demo.service.ChangeRequestRoleService;
import com.example.demo.service.impl.FlowManagementService;
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

    /**
     * Gets all roles by change request id.
     *
     * @param id the id
     * @return the all roles by change request id
     */
    @GetMapping("/{id}/change-roles")
    public ResponseEntity<List<ChangeRequestRoleModel>> getAllRolesByChangeRequestId(
            @PathVariable Long id) {
        var roles = changeRequestRoleService.findAllByChangeRequestId(id);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/change-flow/{changeFlowId}/change-flow-node/{changeFlowNodeId}")
    public ResponseEntity<FlowNavigationInfo> findCurrentAndNextNodeByCurrentPoint(
            @PathVariable Long changeFlowId,
            @PathVariable("changeFlowNodeId") String changeFlowNodeId,
            @RequestParam(value = "handleId", required = false) String handleId) {
        var flowNodeModel = flowManagementService.findCurrentAndNextNodeByCurrentPoint(changeFlowId,
                changeFlowNodeId, handleId);
        return ResponseEntity.ok(flowNodeModel);
    }
}