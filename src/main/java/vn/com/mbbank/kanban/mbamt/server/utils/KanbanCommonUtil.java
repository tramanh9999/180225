package vn.com.mbbank.kanban.mbamt.server.utils;

import java.util.List;

public class KanbanCommonUtil {
    public static boolean listIsEmptyOrNull(List<?> oneGroup) {
        return oneGroup == null || oneGroup.isEmpty();
    }
}
