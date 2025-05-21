package com.example.demo.controller;

import com.example.demo.dtos.GroupDto;
import com.example.demo.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/change-role")
    public ResponseEntity<Page<GroupDto>> getChangeRoleGroups(Pageable pageable) {
        return ResponseEntity.ok(groupService.getChangeRoleGroups(pageable));
    }
}
