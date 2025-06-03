package com.example.demo.service.dto;

import lombok.Data;

import java.text.MessageFormat;


@Data
public class BusinessException extends RuntimeException {
    private final ErrorCodeCommon code;

    private String customMessage;

    public BusinessException(ErrorCodeCommon code) {
        super(code.getMessage());
        this.code = code;
    }

    public BusinessException(ErrorCodeCommon code, Object... params) {
        this.customMessage = MessageFormat.format(code.getCode(), params);
        this.code = code;
    }
}
