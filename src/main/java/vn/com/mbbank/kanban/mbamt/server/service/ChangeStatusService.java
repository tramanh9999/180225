package vn.com.mbbank.kanban.mbamt.server.service;

import vn.com.mbbank.kanban.mbamt.server.enums.ChangeStage;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeStatusModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ChangeStatusService {

    @Transactional(readOnly = true)
    Optional<ChangeStatusModel> getChangeStatusById(Long id);

    @Transactional(readOnly = true)
    Optional<ChangeStatusModel> getChangeStatusByName(String name);

    List<ChangeStatusModel> findAllChangeStatusInSameStage(Long statusId);

    @Transactional(readOnly = true)
    List<ChangeStatusModel> getAllChangeStatuses();


    @Transactional(readOnly = true)
    List<ChangeStatusModel> getChangeStatusesByStage(ChangeStage stage);
}
