package com.example.demo.service;

import com.example.demo.enums.ChangeStage;
import com.example.demo.model.ChangeStatusModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ChangeStatusService {

    @Transactional(readOnly = true)
    Optional<ChangeStatusModel> getChangeStatusById(Long id);

    @Transactional(readOnly = true)
    Optional<ChangeStatusModel> getChangeStatusByName(String name);

    List<ChangeStatusModel> findRemainingStatusesInSameStage(Long statusId);

    @Transactional(readOnly = true)
    List<ChangeStatusModel> getAllChangeStatuses();


    @Transactional(readOnly = true)
    List<ChangeStatusModel> getChangeStatusesByStage(ChangeStage stage);
}
