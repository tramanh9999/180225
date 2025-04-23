package com.example.demo.controller;

import com.example.demo.dtos.SomeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/modules/{moduleAction}")
public class MyModuleController {

        @GetMapping
        @PreAuthorize("@aclSvc.hasPermission(T(com.example.demo.constants.PermissionActionModule).GROUP, T(com.example.demo.constants.PermissionAction)"
                        +
                        ".VIEW_LIST, T(com.example.demo.constants.PermissionActionType).MODULE)")
        public ResponseEntity<?> getList(@PathVariable String moduleAction) {
                // ... logic lấy danh sách
                return null;
        }

        @GetMapping("/{resourceId}")
        @PreAuthorize("@aclSvc.hasPermission(#resourceId, T(com.example.demo.constants.PermissionActionModule).GROUP, T(com.example.demo.constants.PermissionAction).VIEW, T(com.example.demo.constants.PermissionActionType).MODULE)")
        public ResponseEntity<?> getView(@PathVariable String moduleAction,
                        @PathVariable Long resourceId) {
                // ... logic xem chi tiết
                return null;
        }

        @PostMapping
        @PreAuthorize("@aclSvc.hasPermission(T(com.example.demo.constants.PermissionActionModule).GROUP," +
                        " T(com.example.demo.constants.PermissionAction).CREATE, T(com.example.demo" +
                        ".constants" + ".PermissionActionType).MODULE)")
        public ResponseEntity<?> create(@PathVariable String moduleAction, @RequestBody SomeDto data) {
                // ... logic tạo mới
                return null;
        }

        @PutMapping("/{resourceId}")
        @PreAuthorize("@aclSvc.hasPermission(#resourceId, T(com.example.demo.constants.PermissionActionModule).GROUP, "
                        +
                        "T(com.example.demo.constants.PermissionAction).UPDATE, T(com.example.demo.constants.PermissionActionType).MODULE)")
        public ResponseEntity<?> update(@PathVariable String moduleAction,
                        @PathVariable Long resourceId, @RequestBody SomeDto data) {
                // ... logic cập nhật
                return null;
        }

        @DeleteMapping("/{resourceId}")
        @PreAuthorize("@aclSvc.hasPermission(#resourceId, T(com.example.demo.constants.PermissionActionModule).GROUP, T(com.example.demo.constants.PermissionAction).DELETE, T(com.example.demo.constants.PermissionActionType).MODULE)")
        public ResponseEntity<?> delete(@PathVariable String moduleAction,
                        @PathVariable Long resourceId) {
                // ... logic xóa
                return null;
        }
}
