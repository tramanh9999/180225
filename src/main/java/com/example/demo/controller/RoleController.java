package com.example.demo.controller;

import com.example.demo.model.PagingRequestModel;
import com.example.demo.model.RoleModel;
import com.example.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("")
    public ResponseEntity<Page<RoleModel>> findRoles(@RequestBody PagingRequestModel request) {
        return ResponseEntity.ok(roleService.findRoles(request));
    }

    /**
     * Get users in role by roleId with paging
     *
     * @param roleId
     * @param request
     * @return
     */
    @PostMapping("/{roleId}/users")
    public ResponseEntity<Page<RoleModel>> findUsersInRole(@PathVariable Long roleId, @RequestBody
    PagingRequestModel request) {
        return ResponseEntity.ok(roleService.findUsersInRole(roleId, request));
    }
}
