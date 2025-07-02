package com.example.demo.service;

import com.example.demo.entity.ChangeStatusEntity;
import com.example.demo.enums.ChangeStage;
import com.example.demo.mapper.ChangeStatusMapper;
import com.example.demo.model.BusinessException;
import com.example.demo.model.ChangeStatusModel;
import com.example.demo.model.ErrorCodeCommon;
import com.example.demo.repository.ChangeStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

@RequiredArgsConstructor


public class ChangeStatusServiceImpl implements ChangeStatusService {

    private final ChangeStatusRepository changeStatusRepository;
    private final ChangeStatusMapper changeStatusMapper;

    /**
     * Retrieves a ChangeStatus by its ID.
     *
     * @param id The ID of the change status.
     * @return An Optional containing the ChangeStatusModel if found, or empty otherwise.
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<ChangeStatusModel> getChangeStatusById(Long id) {
        return changeStatusRepository.findById(id).map(changeStatusMapper::toModel);
    }

    /**
     * Retrieves a ChangeStatus by its unique name.
     *
     * @param name The name of the change status.
     * @return An Optional containing the ChangeStatusModel if found, or empty otherwise.
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<ChangeStatusModel> getChangeStatusByName(String name) {
        return changeStatusRepository.findByName(name).map(changeStatusMapper::toModel);
    }


    /**
     * Tìm tất cả các ChangeStatusModel trong cùng một Stage với statusId đầu vào,
     * nhưng loại trừ chính statusId đó.
     *
     * @param statusId ID của ChangeStatusEntity dùng làm điểm tham chiếu.
     *                 Status này sẽ được tìm thấy, stage của nó sẽ được xác định,
     *                 và sau đó tất cả các status khác trong stage đó (ngoại trừ chính nó) sẽ được trả về.
     * @return Danh sách các ChangeStatusModel còn lại trong Stage.
     * @throws BusinessException nếu ChangeStatusEntity với statusId không được tìm thấy,
     *                           hoặc nếu Stage của nó là null.
     */
    @Override
    public List<ChangeStatusModel> findAllChangeStatusInSameStage(Long statusId) {
        if (statusId == null) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_STATUS_ID_NOT_FOUND, statusId);
        }
        //add logic if statusid  == CONSTANTS ChangeStatus.DRAFT=0 then return stage =
        // SUBMISSION_PLANNING
        // Otherwise, find the ChangeStatusEntity by statusId
        // and retrieve all statuses in the same stage.
        // If the statusId is null, throw an exception.
        if (ChangeStatusModel.DRAFT.equals(statusId)) {
            return changeStatusRepository.findByStage(ChangeStage.SUBMISSION_PLANNING).stream()
                    .map(changeStatusMapper::toModel).collect(Collectors.toList());
        }

        Optional<ChangeStatusEntity> optionalCurrentStatus =
                changeStatusRepository.findById(statusId);

        ChangeStatusEntity currentStatusEntity = optionalCurrentStatus.orElseThrow(
                () -> new BusinessException(ErrorCodeCommon.CHANGE_STATUS_NOT_FOUND, statusId));

        ChangeStage currentStage = currentStatusEntity.getStage();


        List<ChangeStatusEntity> allStatusesInStage =
                changeStatusRepository.findByStage(currentStage);

        if (allStatusesInStage == null || allStatusesInStage.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        return allStatusesInStage.stream().map(changeStatusMapper::toModel)
                .collect(Collectors.toList());
    }


    /**
     * Retrieves all ChangeStatuses.
     *
     * @return A list of all ChangeStatusModels.
     */
    @Transactional(readOnly = true)
    @Override
    public List<ChangeStatusModel> getAllChangeStatuses() {
        return changeStatusRepository.findAll().stream().map(changeStatusMapper::toModel)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all ChangeStatuses for a specific stage.
     *
     * @param stage The stage name.
     * @return A list of ChangeStatusModels for the given stage.
     */
    @Transactional(readOnly = true)
    @Override
    public List<ChangeStatusModel> getChangeStatusesByStage(ChangeStage stage) {
        return changeStatusRepository.findByStage(stage).stream().map(changeStatusMapper::toModel)
                .collect(Collectors.toList());
    }


}