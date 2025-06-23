package com.example.demo.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the type of handle, either input or output.
 * It can also represent a default state when the handle type is not specified.
 */
@Getter
@RequiredArgsConstructor
public enum HandleType {
    INPUT("input"), OUTPUT("output"), DEFAULT(null);

    private final String value;

    public static HandleType fromString(String handleString) {
        if (handleString == null) {
            return DEFAULT;
        }
        if (handleString.endsWith("-input")) {
            return INPUT;
        }
        if (handleString.endsWith("-output")) {
            return OUTPUT;
        }
        return DEFAULT;
    }
}
