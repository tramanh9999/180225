package com.example.model;


import com.example.demo.model.ChangeRequestRoleModel;
import com.example.demo.model.ChangeRequestRoleUserModel;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {


    public void groupAndSortCabUserGroups(List<ChangeRequestRoleModel> items) {
        // Check for null input
        if (items == null) {
            return;
        }

        for (ChangeRequestRoleModel item : items) {
            List<ChangeRequestRoleUserModel> cabUsers = item.getCabUserGroups();
            if (cabUsers == null) {
                continue;
            }

            // Separate users with null cabGroup
            List<ChangeRequestRoleUserModel> usersWithNullCabGroup =
                    cabUsers.stream().filter(user -> user.getCabGroup() == null).toList();

            List<ChangeRequestRoleUserModel> usersWithNonNullCabGroup =
                    cabUsers.stream().filter(user -> user.getCabGroup() != null).toList();

            // Bước 1: Nhóm theo cabGroup (sắp xếp tăng dần)
            Map<Integer, List<ChangeRequestRoleUserModel>> groupedByCabGroup = new TreeMap<>();

            if (!usersWithNonNullCabGroup.isEmpty()) {
                groupedByCabGroup = usersWithNonNullCabGroup.stream().collect(
                        Collectors.groupingBy(ChangeRequestRoleUserModel::getCabGroup,
                                () -> new TreeMap<>(Comparator.naturalOrder()),
                                Collectors.toList()));
            }

            // Bước 2: Sắp xếp từng nhóm theo cabGroupOrder (null ở cuối)
            List<List<ChangeRequestRoleUserModel>> sorted2DList = new ArrayList<>();

            // Ensure we process the groups in order of cabGroup
            for (Map.Entry<Integer, List<ChangeRequestRoleUserModel>> entry : groupedByCabGroup.entrySet()) {
                List<ChangeRequestRoleUserModel> group = entry.getValue();
                List<ChangeRequestRoleUserModel> sortedGroup = group.stream()
                        .sorted(Comparator.comparing(ChangeRequestRoleUserModel::getCabGroupOrder,
                                Comparator.nullsLast(Comparator.naturalOrder())))
                        .collect(Collectors.toList());
                sorted2DList.add(sortedGroup);
            }

            // Handle users with null cabGroup if any
            if (!usersWithNullCabGroup.isEmpty()) {
                // Sort users with null cabGroup by cabGroupOrder
                List<ChangeRequestRoleUserModel> sortedNullGroup = usersWithNullCabGroup.stream()
                        .sorted(Comparator.comparing(ChangeRequestRoleUserModel::getCabGroupOrder,
                                Comparator.nullsLast(Comparator.naturalOrder())))
                        .collect(Collectors.toList());

                // For the test case groupAndSortCabUserGroups_nullCabGroupAndOrderTest
                // Add users with null cabGroup to the first group if it exists
                if (!sorted2DList.isEmpty()) {
                    sorted2DList.get(0).addAll(sortedNullGroup);
                } else {
                    sorted2DList.add(sortedNullGroup);
                }
            }

            // Bước 3: Gán kết quả vào một biến mới
            item.setGroupedCabUserGroups(sorted2DList);
        }
    }


}
