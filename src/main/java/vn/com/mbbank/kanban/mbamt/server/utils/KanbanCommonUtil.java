package vn.com.mbbank.kanban.mbamt.server.utils;

import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestRoleUserModel;

import java.util.List;

public class KanbanCommonUtil {
    public static boolean listIsEmptyOrNull(List<ChangeRequestRoleUserModel> oneGroup) {
        return oneGroup == null || oneGroup.isEmpty();
    }
}
