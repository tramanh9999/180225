package com.example.demo.service.dto;

public class BusinessException extends RuntimeException {
    private final ErrorCodeCommon errorCodeCommon;

    public BusinessException(ErrorCodeCommon errorCodeCommon) {
        super(errorCodeCommon.getMessage());
        this.errorCodeCommon = errorCodeCommon;
    }

    public ErrorCodeCommon getErrorCodeCommon() {
        return errorCodeCommon;
    }
}
