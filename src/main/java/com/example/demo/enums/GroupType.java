package com.example.demo.enums;

public enum GroupType {
    NORMAL("NORMAL", "Nhóm thông thường"),
    CHANGE_ROLE("CHANGE_ROLE", "Nhóm thay đổi vai trò");

    private final String value;
    private final String description;

    GroupType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}