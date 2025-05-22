package com.example.demo.controller;

import com.example.demo.model.GroupModel;
import com.example.demo.model.PagingRequestModel;
import com.example.demo.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/change-role")
    public ResponseEntity<Page<GroupModel>> getChangeRoleGroups(Pageable pageable) {
        return ResponseEntity.ok(groupService.getChangeRoleGroups(pageable));
    }

    @PostMapping("")
    public ResponseEntity<Page<GroupModel>> getGroups(@RequestBody PagingRequestModel request) {
        return ResponseEntity.ok(groupService.getGroups(request));
    }
}
