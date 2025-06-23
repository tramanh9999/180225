package com.example.demo.enums;

public enum HandleType {
    INPUT("input"), OUTPUT("output"),
    DEFAULT(null); // Cho các handle mặc định hoặc không xác định rõ

    private final String value;

    HandleType(String value) {
        this.value = value;
    }

    // Phương thức tiện ích để chuyển đổi từ String sang HandleType
    // Phương thức này chỉ phân loại kiểu handle dựa trên hậu tố
    public static HandleType fromString(String handleString) {
        if (handleString == null) {
            return DEFAULT;
        }
        if (handleString.endsWith("-input")) {
            return INPUT;
        }
        if (handleString.endsWith("-output")) { // Kiểm tra output chung sau các output cụ thể
            return OUTPUT;
        }
        // Thêm các loại handle khác nếu có trong tương lai
        return DEFAULT; // Trả về mặc định nếu không khớp
    }

    public String getValue() {
        return value;
    }
}
