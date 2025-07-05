package vn.com.mbbank.kanban.mbamt.server.controller;

import vn.com.mbbank.kanban.mbamt.server.constants.ServerUrl;
import vn.com.mbbank.kanban.mbamt.server.model.PagingRequestModel;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeFlowNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing Change Request Roles.
 */
@RestController
@RequestMapping(ServerUrl.CHANGE_FLOW_NODES)
@RequiredArgsConstructor
public class ChangeFlowNodeController {

    private final ChangeFlowNodeService changeFlowNodeService;

    /**
     * findPagingUsernameById.
     *
     * @param id           the id of the change flow node.
     * @param requestModel the paging request model.
     * @return the page of usernames.
     */
    @GetMapping("/{id}/users")
    public ResponseEntity<Page<String>> findPagingUsernameById(@PathVariable Long id, @RequestBody
    PagingRequestModel requestModel) {
        Page<String> pagings = changeFlowNodeService.findPagingUsernameById(id, requestModel);
        return ResponseEntity.ok(pagings);
    }
}