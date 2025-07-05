package vn.com.mbbank.kanban.mbamt.server.controller;

import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestApprovalModel;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeStatusModel;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeRequestApprovalService;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
public class ChangeRequestApprovalController {

    @Autowired
    private ChangeRequestApprovalService approvalService;


    @Autowired
    private ChangeStatusService changeStatusService;

    @GetMapping
    public ResponseEntity<List<ChangeRequestApprovalModel>> getAllApprovals() {
        List<ChangeRequestApprovalModel> approvals = approvalService.findAll();
        return new ResponseEntity<>(approvals, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChangeRequestApprovalModel> getApprovalById(@PathVariable Long id) {
        ChangeRequestApprovalModel approval = approvalService.findById(id);
        if (approval != null) {
            return new ResponseEntity<>(approval, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<List<ChangeRequestApprovalModel>> createApproval(
            @RequestBody ChangeRequestApprovalModel approvalDto) {
        List<ChangeRequestApprovalModel> createdApproval =
                approvalService.saveList(List.of(approvalDto));
        return new ResponseEntity<>(createdApproval, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<List<ChangeRequestApprovalModel>> updateApproval(@PathVariable Long id,
                                                                           @RequestBody
                                                                           ChangeRequestApprovalModel approvalDto) {
        // Check if approval exists
        if (approvalService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        approvalDto.setId(id);
        List<ChangeRequestApprovalModel> updatedApproval =
                approvalService.saveList(List.of(approvalDto));
        return new ResponseEntity<>(updatedApproval, HttpStatus.OK);
    }


    // api find all status in same stage with input status id
    @GetMapping("/{statusId}/related-statuses")
    public ResponseEntity<List<ChangeStatusModel>> getApprovalsByStatus(
            @PathVariable Long statusId) {
        var approvals = changeStatusService.findAllChangeStatusInSameStage(statusId);
        return new ResponseEntity<>(approvals, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApproval(@PathVariable Long id) {
        // Check if approval exists
        if (approvalService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        approvalService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
