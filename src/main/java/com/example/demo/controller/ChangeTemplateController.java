package com.example.demo.controller;

import com.example.demo.dtos.ChangeTemplateModel;
import com.example.demo.dtos.ChangeTemplateFieldItemDto;
import com.example.demo.service.ChangeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Change Templates.
 */
@RestController
@RequestMapping("/change-template")
public class ChangeTemplateController {

    @Autowired
    private ChangeTemplateService changeTemplateService;

    /**
     * Endpoint to find paginated Change Templates.
     *
     * @param paginationRequest The pagination request.
     * @return A page of ChangeTemplateModel.
     */
    @GetMapping("/pagings")
    public ResponseEntity<Page<ChangeTemplateModel>> findPagings(@RequestBody Object paginationRequest) {
        Page<ChangeTemplateModel> pagings = changeTemplateService.findPagings(paginationRequest);
        return ResponseEntity.ok(pagings);
    }

    /**
     * Endpoint to delete Change Templates by IDs.
     *
     * @param ids The list of IDs to delete.
     * @return ResponseEntity with status 200 if successful.
     */
    @DeleteMapping("/{ids}")
    public ResponseEntity<Void> deleteByIds(@PathVariable List<Long> ids) {
        changeTemplateService.deleteByIds(ids);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to find a Change Template by ID.
     *
     * @param id The ID of the Change Template.
     * @return ResponseEntity with the ChangeTemplateModel.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChangeTemplateModel> findById(@PathVariable Long id) {
        ChangeTemplateModel changeTemplate = changeTemplateService.findById(id);
        return ResponseEntity.ok(changeTemplate);
    }

    /**
     * Endpoint to save a new Change Template.
     *
     * @param changeTemplateModel The ChangeTemplateModel to save.
     * @return ResponseEntity with the saved ChangeTemplateModel.
     */
    @PostMapping
    public ResponseEntity<ChangeTemplateModel> save(@RequestBody ChangeTemplateModel changeTemplateModel) {
        ChangeTemplateModel savedChangeTemplate = changeTemplateService.save(changeTemplateModel);
        return ResponseEntity.ok(savedChangeTemplate);
    }

    /**
     * Endpoint to save an existing Change Template by ID.
     *
     * @param id                  The ID of the Change Template to update.
     * @param changeTemplateModel The updated ChangeTemplateModel.
     * @return ResponseEntity with the saved ChangeTemplateModel.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChangeTemplateModel> save(@PathVariable Long id,
            @RequestBody ChangeTemplateModel changeTemplateModel) {
        ChangeTemplateModel savedChangeTemplate = changeTemplateService.save(id, changeTemplateModel);
        return ResponseEntity.ok(savedChangeTemplate);
    }

    /**
     * Endpoint to get paginated field item data for a Change Template.
     *
     * @param changeTemplateId The ID of the Change Template.
     * @param page             The page number (0-indexed).
     * @param size             The number of items per page.
     * @return A page of field item data.
     */
    @GetMapping("/{changeTemplateId}/field-items")
    public ResponseEntity<Page<ChangeTemplateFieldItemDto>> getPaginatedFieldItems(
            @PathVariable Long changeTemplateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ChangeTemplateFieldItemDto> fieldItemsPage = changeTemplateService.getPaginatedFieldItems(changeTemplateId,
                page, size);
        return ResponseEntity.ok(fieldItemsPage);
    }
}
