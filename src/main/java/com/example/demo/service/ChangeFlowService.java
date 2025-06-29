package com.example.demo.service;

import com.example.demo.enums.ChangeStage;
import com.example.demo.model.ChangeFlowModel;

import java.util.Optional;

public interface ChangeFlowService {
    Optional<ChangeFlowModel> findById(Long id);

    String findCurrentNodeIdByChangeFlowIdAndStage(Long changeFlowId, ChangeStage stage);
}
