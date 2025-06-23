package com.example.demo.controller;

import com.example.demo.model.ChangeRequestApprovalModel;
import com.example.demo.service.ChangeRequestApprovalService;
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
    public ResponseEntity<ChangeRequestApprovalModel> createApproval(
            @RequestBody ChangeRequestApprovalModel approvalDto) {
        ChangeRequestApprovalModel createdApproval = approvalService.save(approvalDto);
        return new ResponseEntity<>(createdApproval, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChangeRequestApprovalModel> updateApproval(@PathVariable Long id,
                                                                     @RequestBody
                                                                     ChangeRequestApprovalModel approvalDto) {
        // Check if approval exists
        if (approvalService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        approvalDto.setId(id);
        ChangeRequestApprovalModel updatedApproval = approvalService.save(approvalDto);
        return new ResponseEntity<>(updatedApproval, HttpStatus.OK);
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

    @GetMapping("/by-change-request/{changeRequestId}")
    public ResponseEntity<List<ChangeRequestApprovalModel>> getApprovalsByChangeRequestId(
            @PathVariable Long changeRequestId) {
        List<ChangeRequestApprovalModel> approvals =
                approvalService.findByChangeRequestId(changeRequestId);
        return new ResponseEntity<>(approvals, HttpStatus.OK);
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<ChangeRequestApprovalModel>> getApprovalsByStatus(
            @PathVariable String status) {
        List<ChangeRequestApprovalModel> approvals = approvalService.findByOverallStatus(status);
        return new ResponseEntity<>(approvals, HttpStatus.OK);
    }

    @GetMapping("/by-change-request/{changeRequestId}/status/{status}")
    public ResponseEntity<List<ChangeRequestApprovalModel>> getApprovalsByChangeRequestAndStatus(
            @PathVariable Long changeRequestId, @PathVariable String status) {
        List<ChangeRequestApprovalModel> approvals =
                approvalService.findByChangeRequestIdAndOverallStatus(changeRequestId, status);
        return new ResponseEntity<>(approvals, HttpStatus.OK);
    }
}
