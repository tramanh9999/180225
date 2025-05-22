package com.example.demo.service.dto;

import org.springframework.http.HttpStatus;

public class ErrorCodeCommon {
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

    // Các lỗi validate
    public static final ErrorCodeCommon NAME_REQUIRED = new ErrorCodeCommon("NAME_REQUIRED",
            "Tên mẫu thay đổi không được để trống", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon ROLE_GROUP_ID_REQUIRED = new ErrorCodeCommon("ROLE_GROUP_ID_REQUIRED",
            "Mỗi role phải có groupId", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon ROLE_LEVEL_REQUIRED = new ErrorCodeCommon("ROLE_LEVEL_REQUIRED",
            "Mỗi role phải có level", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon USERNAME_REQUIRED = new ErrorCodeCommon("USERNAME_REQUIRED",
            "Username trong role không được để trống", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon GROUP_ID_NOT_FOUND = new ErrorCodeCommon("GROUP_ID_NOT_FOUND",
            "GroupId không tồn tại", HttpStatus.BAD_REQUEST);
    public static final ErrorCodeCommon USERNAME_NOT_FOUND = new ErrorCodeCommon("USERNAME_NOT_FOUND",
            "Username không tồn tại trong hệ thống", HttpStatus.BAD_REQUEST);
}