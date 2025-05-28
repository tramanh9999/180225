package com.example.demo.controller;

import com.example.demo.model.ChangeTemplateFieldItemDto;
import com.example.demo.model.ChangeTemplateModel;
import com.example.demo.model.PagingRequestModel;
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
     * @param pagingRequest The pagination request.
     * @return A page of ChangeTemplateModel.
     */
    @GetMapping("/pagings")
    public ResponseEntity<Page<ChangeTemplateModel>> findPagings(
            @RequestBody PagingRequestModel pagingRequest) {
        Page<ChangeTemplateModel> pagings = changeTemplateService.findPagings(pagingRequest);
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
    public ResponseEntity<ChangeTemplateModel> save(
            @RequestBody ChangeTemplateModel changeTemplateModel) {
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
    public ResponseEntity<ChangeTemplateModel> save(@PathVariable Long id, @RequestBody
    ChangeTemplateModel changeTemplateModel) {
        ChangeTemplateModel savedChangeTemplate =
                changeTemplateService.save(id, changeTemplateModel);
        return ResponseEntity.ok(savedChangeTemplate);
    }


    /**
     * Gets paginated field items.
     *
     * @param changeTemplateId the change template id
     * @param pagingRequest    the paging request
     * @return the paginated field items
     */
    @GetMapping("/{changeTemplateId}/field-items")
    public ResponseEntity<Page<ChangeTemplateFieldItemDto>> getPaginatedFieldItems(
            @PathVariable Long changeTemplateId,
            @RequestBody(required = false) PagingRequestModel pagingRequest) {
        int page = pagingRequest != null ? pagingRequest.getPage() : 0;
        int size = pagingRequest != null ? pagingRequest.getSize() : 10;
        Page<ChangeTemplateFieldItemDto> fieldItemsPage =
                changeTemplateService.getPaginatedFieldItems(changeTemplateId, page, size);
        return ResponseEntity.ok(fieldItemsPage);
    }

    /**
     * Endpoint to get detail with roles for a Change Template.
     *
     * @param id         The ID of the Change Template.
     * @param userPaging The paging request for users.
     * @return ResponseEntity with the ChangeTemplateModel.
     */
    @GetMapping("/{id}/detail")
    public ResponseEntity<ChangeTemplateModel> getDetailWithRoles(@PathVariable Long id,
                                                                  @RequestBody(required = false)
                                                                  PagingRequestModel userPaging) {
        int userPage = userPaging != null ? userPaging.getPage() : 0;
        int userSize = userPaging != null ? userPaging.getSize() : 10;
        ChangeTemplateModel model =
                changeTemplateService.getDetailWithRoles(id, userPage, userSize);
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(model);
    }

    /**
     * Endpoint to get detail with roles for a Change Template.
     *
     * @param id The ID of the Change Template.
     * @return ResponseEntity with the ChangeTemplateModel.
     */
    @GetMapping("/{id}/detail-new")
    public ResponseEntity<ChangeTemplateModel> getDetailWithRolesNew(@PathVariable Long id) {
        ChangeTemplateModel model = changeTemplateService.getDetailWithRoles(id);
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(model);
    }

}
