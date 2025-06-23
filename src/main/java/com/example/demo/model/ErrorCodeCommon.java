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
