package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model đại diện cho một cấp phê duyệt (approval level) trong change template.
 * Tương thích với approvalLevelSchema phía frontend.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LevelGroupModel {
    /**
     * Id của cấp phê duyệt
     */
    private String id;
    /**
     * Tiêu đề cấp phê duyệt
     */
    private String title;

    /**
     * Danh sách group thực hiện phê duyệt ở cấp này
     */
    private List<SysGroupModel> changeRoles;
}
