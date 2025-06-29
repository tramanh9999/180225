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
import java.util.Objects;
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
    public List<ChangeStatusModel> findRemainingStatusesInSameStage(Long statusId) {
        // 1. Validate input statusId
        if (statusId == null) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_NOT_FOUND,
                    "Status ID cannot be null.");
        }

        // 2. Tìm ChangeStatusEntity dựa trên statusId đầu vào
        Optional<ChangeStatusEntity> optionalCurrentStatus =
                changeStatusRepository.findById(statusId);

        ChangeStatusEntity currentStatusEntity = optionalCurrentStatus.orElseThrow(
                () -> new BusinessException(ErrorCodeCommon.CHANGE_STATUS_NOT_FOUND,
                        // Cần định nghĩa mã lỗi này
                        statusId));

        // 3. Lấy ChangeStage từ ChangeStatusEntity hiện tại
        ChangeStage currentStage = currentStatusEntity.getStage();


        // 4. Tìm tất cả các ChangeStatusEntity thuộc về Stage đó
        // Giả định ChangeStatusRepository có phương thức để tìm theo Stage
        List<ChangeStatusEntity> allStatusesInStage =
                changeStatusRepository.findByStage(currentStage);

        // 5. Chuyển đổi sang Model và lọc ra statusId ban đầu
        if (allStatusesInStage == null || allStatusesInStage.isEmpty()) {
            return java.util.Collections.emptyList(); // Không có status nào trong stage (ngoại trừ statusId ban đầu, hoặc không có gì cả)
        }

        return allStatusesInStage.stream()
                // Lọc bỏ status có ID trùng với statusId đầu vào
                .filter(status -> !Objects.equals(status.getId(), statusId))
                // Chuyển đổi từ Entity sang Model/DTO
                .map(changeStatusMapper::toModel) // Sử dụng hàm helper để convert
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