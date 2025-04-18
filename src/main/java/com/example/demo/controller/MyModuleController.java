package com.example.demo.controller;


import com.example.demo.dtos.SomeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/modules/{moduleAction}")
public class MyModuleController {

    @GetMapping
    @PreAuthorize("hasPermission(#moduleAction, 'VIEW_LIST', 'MODULE')")
    public ResponseEntity<?> getList(@PathVariable String moduleAction) {
        // ... logic lấy danh sách
    }

    @GetMapping("/{resourceId}")
    @PreAuthorize("hasPermission(#resourceId, #moduleAction, 'VIEW', 'MODULE')")
    public ResponseEntity<?> getView(@PathVariable String moduleAction,
                                     @PathVariable Long resourceId) {
        // ... logic xem chi tiết
    }

    @PostMapping
    @PreAuthorize("hasPermission(#moduleAction, 'CREATE', 'MODULE')")
    public ResponseEntity<?> create(@PathVariable String moduleAction, @RequestBody SomeDto data) {
        // ... logic tạo mới
    }

    @PutMapping("/{resourceId}")
    @PreAuthorize("hasPermission(#resourceId, #moduleAction, 'UPDATE', 'MODULE')")
    public ResponseEntity<?> update(@PathVariable String moduleAction,
                                    @PathVariable Long resourceId, @RequestBody SomeDto data) {
        // ... logic cập nhật
    }

    @DeleteMapping("/{resourceId}")
    @PreAuthorize("hasPermission(#resourceId, #moduleAction, 'DELETE', 'MODULE')")
    public ResponseEntity<?> delete(@PathVariable String moduleAction,
                                    @PathVariable Long resourceId) {
        // ... logic xóa
    }
}
