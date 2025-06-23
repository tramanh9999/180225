package com.example.demo.model;

import com.example.demo.enums.HandleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Đây là một DTO, không phải Entity, nên không cần @Entity hay @Column
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedHandleModel {
    private String rawHandleId; // Chuỗi handle gốc từ React Flow
    private HandleType type; // Loại handle (INPUT, OUTPUT, ACCEPT_OUTPUT, REJECT_OUTPUT)
    private Long changeStatusId; // ID từ bảng ChangeStatus (nếu có, null nếu không)
    private String customAction;
    // Ví dụ: "Accept", "Reject" (đối với các handle như APPROVAL_NODE-...-Accept-output)

    // Constructor và các phương thức khác
}
