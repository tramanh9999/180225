package com.example.demo.controller;

import com.example.demo.constants.ServerUrl;
import com.example.demo.model.ChangeRequestRoleModel;
import com.example.demo.model.FlowNodeModel;
import com.example.demo.model.NextFlowTargetModel;
import com.example.demo.service.ChangeRequestRoleService;
import com.example.demo.service.impl.FlowManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{id}/change-status/{changeStatusId}/next-target")
    public ResponseEntity<NextFlowTargetModel> findNextTargetFromChangeStatusIdyChangeRequestId(
            @PathVariable("id") Long changeRequestId,
            @PathVariable("changeStatusId") Long changeStatusId) {
        var nextFlowTargetModel =
                flowManagementService.findNextTargetFromChangeStatusId(changeRequestId,
                        changeStatusId);
        return ResponseEntity.ok(nextFlowTargetModel);
    }

    @GetMapping("/test-change/change-status/{changeStatusId}/next-target")
    public ResponseEntity<FlowNodeModel> testNextTargetFromChangeStatusIdyChangeRequestId(
            @PathVariable("changeStatusId") Long changeStatusId) {
        var nextFlowTargetModel = flowManagementService.getNextNodeDetails(1L, changeStatusId);
        return ResponseEntity.ok(nextFlowTargetModel);
    }
}