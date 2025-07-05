package vn.com.mbbank.kanban.mbamt.server.utils;


public class ChangeRequestRoleUtils {


//    public static void groupAndSortCabUserGroups2(List<ChangeRequestRoleModel> items) {
//        if (items == null || items.isEmpty()) {
//            return;
//        }
//
//        for (ChangeRequestRoleModel item : items) {
//            List<ChangeRequestRoleUserModel> cabUsers = item.getCabUserGroups();
//            if (cabUsers == null || cabUsers.isEmpty()) {
//                item.setWorkflowUsers(Collections.emptyList());
//                continue;
//            }
//
//            Map<Long, List<ChangeRequestRoleUserModel>> groupedByWorkflow =
//                    cabUsers.stream().collect(Collectors.groupingBy(user -> {
//                        if (user.getChangeRequestWorkflowId() != null) {
//                            return user.getChangeRequestWorkflowId();
//                        }
//                        return 0L;
//                    }));
//            Set<Long> setWorkflowIds = groupedByWorkflow.keySet();
//
//            List<ChangeRequestRoleUserWorkflowListModel> workflowUsersList = new ArrayList<>();
//            for (Map.Entry<Long, List<ChangeRequestRoleUserModel>> workflowEntry : groupedByWorkflow.entrySet()) {
//                List<ChangeRequestRoleUserModel> usersInCurrentWorkflow = workflowEntry.getValue();
//                List<ChangeRequestRoleUserModel> usersWithNonNullCabGroup =
//                        usersInCurrentWorkflow.stream().filter(user -> user.getCabGroup() != null)
//                                .toList();
//                Map<Integer, List<ChangeRequestRoleUserModel>> groupedByCabGroup = new TreeMap<>();
//                if (!usersWithNonNullCabGroup.isEmpty()) {
//                    groupedByCabGroup = usersWithNonNullCabGroup.stream().collect(
//                            Collectors.groupingBy(ChangeRequestRoleUserModel::getCabGroup,
//                                    () -> new TreeMap<>(Comparator.naturalOrder()),
//                                    Collectors.toList()));
//                }
//                List<List<ChangeRequestRoleUserModel>> sorted2DList = new ArrayList<>();
//                for (Map.Entry<Integer, List<ChangeRequestRoleUserModel>> entry : groupedByCabGroup.entrySet()) {
//                    List<ChangeRequestRoleUserModel> group = entry.getValue();
//                    List<ChangeRequestRoleUserModel> sortedGroup = group.stream()
//                            .sorted(Comparator.comparing(
//                                    ChangeRequestRoleUserModel::getCabGroupOrder,
//                                    Comparator.nullsLast(Comparator.naturalOrder())))
//                            .collect(Collectors.toList());
//                    sorted2DList.add(sortedGroup);
//                }
//                List<ChangeRequestRoleUserListModel> cabUserListModels = sorted2DList.stream()
//                        .map(oneGroup -> ChangeRequestRoleUserListModel.builder().users(oneGroup)
//                                .build()).collect(Collectors.toList());
//                workflowUsersList.add(ChangeRequestRoleUserWorkflowListModel.builder()
//                        .changeWorkflowId(workflowEntry.getKey()).workflows(cabUserListModels)
//                        .build());
//            }
//            item.setWorkflowUsers(workflowUsersList);
//        }
//    }

}
