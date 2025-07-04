package com.example.demo.model;

import org.springframework.http.HttpStatus;

public class ErrorCodeCommon {
    public static final ErrorCodeCommon CHANGE_REQUEST_ID_REQUIRED =
            new ErrorCodeCommon("CHANGE_TEMPLATE_ID_REQUIRED",
                    "Change request ID must not be null or empty", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_REQUEST_ID_NOT_FOUND =
            new ErrorCodeCommon("CHANGE_REQUEST_ID_NOT_FOUND", "Change request ID: {0} not found",
                    HttpStatus.BAD_REQUEST);


    // Các lỗi validate
    public static final ErrorCodeCommon NAME_REQUIRED =
            new ErrorCodeCommon("NAME_REQUIRED", "Tên mẫu thay đổi không được để trống",
                    HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon ROLE_GROUP_ID_REQUIRED =
            new ErrorCodeCommon("ROLE_GROUP_ID_REQUIRED", "Mỗi role phải có groupId",
                    HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon ROLE_LEVEL_REQUIRED =
            new ErrorCodeCommon("ROLE_LEVEL_REQUIRED", "Mỗi role phải có level",
                    HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon USERNAME_REQUIRED =
            new ErrorCodeCommon("USERNAME_REQUIRED", "Username trong role không được để trống",
                    HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon GROUP_ID_NOT_FOUND =
            new ErrorCodeCommon("GROUP_ID_NOT_FOUND", "GroupId không tồn tại",
                    HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon USERNAME_NOT_FOUND =
            new ErrorCodeCommon("USERNAME_NOT_FOUND", "Username {0} không tồn tại trong hệ thống",
                    HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon INVALID_ENUM_VALUE =
            new ErrorCodeCommon("INVALID_ENUM_VALUE", "Giá trị enum không hợp lệ",
                    HttpStatus.BAD_REQUEST);
    // New error codes for ID validation and duplicate name validation
    public static final ErrorCodeCommon ID_NOT_FOUND =
            new ErrorCodeCommon("ID_NOT_FOUND", "ID không tồn tại", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon DUPLICATE_NAME =
            new ErrorCodeCommon("DUPLICATE_NAME", "Tên đã tồn tại trong hệ thống",
                    HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_FLOW_NODE_ID_REQUIRED =
            new ErrorCodeCommon("CHANGE_FLOW_NODE_ID_REQUIRED",
                    "Change flow node ID must not be null or empty", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_FLOW_NODE_ID_NOT_FOUND =
            new ErrorCodeCommon("CHANGE_FLOW_NODE_ID_NOT_FOUND",
                    "Change flow node ID: {0} not found", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_REQUEST_WORKFLOW_ID_REQUIRED =
            new ErrorCodeCommon("CHANGE_REQUEST_WORKFLOW_ID_REQUIRED",
                    "Change request workflow ID must not be null or empty", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_REQUEST_WORKFLOW_ID_NOT_FOUND =
            new ErrorCodeCommon("CHANGE_REQUEST_WORKFLOW_ID_NOT_FOUND",
                    "Change request workflow ID: {0} not found", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_FLOW_NOT_FOUND =
            new ErrorCodeCommon("1111", "ChangeFlowEntity not found with id: {0}",
                    HttpStatus.NOT_FOUND);
    public static final ErrorCodeCommon CHANGE_STATUS_ID_NOT_FOUND =
            new ErrorCodeCommon("CHANGE_STATUS_ID_NOT_FOUND", "Change status ID: {0} not found",
                    HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_FLOW_ID_NOT_FOUND =
            new ErrorCodeCommon("CHANGE_FLOW_ID_NOT_FOUND", "Change flow ID: {0} not found",
                    HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_REQUEST_TEMPLATE_HAS_CHANGE =
            new ErrorCodeCommon("CHANGE_REQUEST_TEMPLATE_HAS_CHANGE",
                    "Change request template has change with id: {0}", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CUSTOM_MESSAGE =
            new ErrorCodeCommon("1001", "Change request template has change with id: {0}",
                    HttpStatus.BAD_REQUEST);


    public static final ErrorCodeCommon CHANGE_REQUEST_NOT_FOUND =
            new ErrorCodeCommon("2001", "Change Request with ID: {0} not found.",
                    HttpStatus.NOT_FOUND);

    public static final ErrorCodeCommon CHANGE_REQUEST_APPROVAL_INPUT_REQUIRED =
            new ErrorCodeCommon("2002", "Approval reply model or status cannot be null.",
                    HttpStatus.BAD_REQUEST);

    public static final ErrorCodeCommon CHANGE_TEMPLATE_ID_MISSING = new ErrorCodeCommon("2003",
            "Change Request with ID: {0} has no associated change template ID.",
            HttpStatus.INTERNAL_SERVER_ERROR); // Or BAD_REQUEST if client should provide it

    public static final ErrorCodeCommon CHANGE_TEMPLATE_NOT_FOUND =
            new ErrorCodeCommon("2004", "Change Template with ID: {0} not found.",
                    HttpStatus.NOT_FOUND);

    public static final ErrorCodeCommon CHANGE_FLOW_CONFIGURATION_ERROR =
            new ErrorCodeCommon("2005", "No change flow ID found for change template: {0}.",
                    HttpStatus.INTERNAL_SERVER_ERROR);

    public static final ErrorCodeCommon NOT_IN_APPROVAL_TIME = new ErrorCodeCommon("2006",
            "Current approval node handle ID is missing for Change Request with ID: {0}.",
            HttpStatus.INTERNAL_SERVER_ERROR);

    public static final ErrorCodeCommon UNSUPPORTED_APPROVAL_STATUS =
            new ErrorCodeCommon("2007", "Unsupported approval status for transition: {0}.",
                    HttpStatus.BAD_REQUEST);

    public static final ErrorCodeCommon CHANGE_REQUEST_APPROVAL_UNREPLIABLE =
            new ErrorCodeCommon("2008", "Change flow not configured properly. " +
                    "Unable to {0} approval request id : {1} in role {2}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
    // Indicates a flow configuration issue


    public static final ErrorCodeCommon FLOW_TRANSITION_EDGE_NOT_FOUND = new ErrorCodeCommon("3002",
            "No outgoing transition edge found for source handle: {0} in Change Flow ID: {1}.",
            HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon INVALID_HANDLE_FORMAT = new ErrorCodeCommon("3003",
            "Invalid handle format: {0}. Expected format is 'nodeId-action-output' or 'nodeId-action-input'.",
            HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_STATUS_NOT_FOUND =
            new ErrorCodeCommon("3001", "Change status with ID: {0} not found.",
                    HttpStatus.NOT_FOUND);
    public static final ErrorCodeCommon INVALID_CHANGE_STATUS =
            new ErrorCodeCommon("200312", "Invalid " + "change status", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon NO_VALID_NEXT_STATUSES_FOUND =
            new ErrorCodeCommon("3001", "No validate change status", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_REQUEST_INVALID_UPDATE_STATUS =
            new ErrorCodeCommon("3001",
                    "Unable to update change request by change status {0} - {1} not in current" +
                            " change stage {2}", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_REQUEST_APPROVAL_ID_REQUIRED =
            new ErrorCodeCommon("CHANGE_REQUEST_APPROVAL_ID_REQUIRED",
                    "Change request approval ID must not be null or empty", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_REQUEST_APPROVAL_NOT_FOUND =
            new ErrorCodeCommon("CHANGE_REQUEST_APPROVAL_NOT_FOUND",
                    "Change request approval with ID: {0} not found", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_REQUEST_APPROVAL_REQUEST_ID_NULL =
            new ErrorCodeCommon("CHANGE_REQUEST_APPROVAL_REQUEST_ID_NULL",
                    "Approval ID: {0} is not in any change request", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_FLOW_CURRENT_EDGE_REQUIRED =
            new ErrorCodeCommon("CHANGE_FLOW_CURRENT_EDGE_REQUIRED",
                    "Current edge in change flow must not be null or empty",
                    HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_REQUEST_REQUIRED =
            new ErrorCodeCommon("CHANGE_REQUEST_REQUIRED",
                    "Change request must not be null or empty", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_REQUEST_ROLE_NOT_CONFIGURED =
            new ErrorCodeCommon("CHANGE_REQUEST_ROLE_NOT_CONFIGURED",
                    "Change request role is not configured for change request with ID: {0}",
                    HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_FLOW_NODES_NOT_FOUND =
            new ErrorCodeCommon("CHANGE_FLOW_NODES_NOT_FOUND",
                    "Change flow nodes not found for the given change flow ID : {0}.",
                    HttpStatus.NOT_FOUND);
    public static final ErrorCodeCommon CHANGE_NODE_ID_REQUIRED =
            new ErrorCodeCommon("CHANGE_NODE_ID_REQUIRED",
                    "Change node ID must not be null or empty", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon CHANGE_REQUEST_USER_NOT_ALLOWED =
            new ErrorCodeCommon("CHANGE_REQUEST_USER_NOT_ALLOWED",
                    "Current user is not allowed to perform this action on approval request ID: {0}",
                    HttpStatus.FORBIDDEN);


    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    public ErrorCodeCommon(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
