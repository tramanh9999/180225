package com.example.demo.service.impl;

import com.example.demo.enums.ChangeStage;
import com.example.demo.mapper.ChangeFlowMapper;
import com.example.demo.model.ChangeFlowModel;
import com.example.demo.repository.ChangeFlowRepository;
import com.example.demo.service.ChangeFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ChangeFlowServiceImpl implements ChangeFlowService {


    private final ChangeFlowRepository changeFlowRepository;
    // Corrected from changeFlowNodeRepository
    private final ChangeFlowMapper changeFlowMapper;
    // Inject a mapper for Entity <-> Model conversion


    @Transactional(readOnly = true)
    @Override
    public Optional<ChangeFlowModel> findById(Long id) {
        return changeFlowRepository.findById(id)
                .map(changeFlowMapper::toModel); // Map Entity to Model
    }

    @Override
    public String findCurrentNodeIdByChangeFlowIdAndStage(Long changeFlowId, ChangeStage stage) {
        return "";
    }
}


