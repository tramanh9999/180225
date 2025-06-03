package com.example.demo.controller;

import com.example.demo.model.PagingRequestModel;
import com.example.demo.model.SysGroupModel;
import com.example.demo.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/change-role")
    public ResponseEntity<Page<SysGroupModel>> getChangeRoleGroups(Pageable pageable) {
        return ResponseEntity.ok(groupService.getChangeRoleGroups(pageable));
    }

    @PostMapping("")
    public ResponseEntity<Page<SysGroupModel>> getGroups(@RequestBody PagingRequestModel request) {
        return ResponseEntity.ok(groupService.getGroups(request));
    }
}
