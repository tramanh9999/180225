package com.example.demo.service.impl;

import com.example.demo.entity.ChangeRequestWorkflowEntity;
import com.example.demo.mapper.ChangeRequestWorkflowMapper;
import com.example.demo.model.ChangeRequestWorkflowModel;
import com.example.demo.repository.ChangeRequestWorkflowRepository;
import com.example.demo.service.ChangeRequestWorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChangeRequestWorkflowServiceImpl implements ChangeRequestWorkflowService {

    private final ChangeRequestWorkflowRepository changeRequestWorkflowRepository;
    private final ChangeRequestWorkflowMapper changeRequestWorkflowMapper;

    @Override
    public Map<Long, ChangeRequestWorkflowModel> findByIdIn(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        List<ChangeRequestWorkflowEntity> entities =
                changeRequestWorkflowRepository.findByIdIn(ids);
        List<ChangeRequestWorkflowModel> models = changeRequestWorkflowMapper.toDtoList(entities);
        return models.stream()
                .collect(Collectors.toMap(ChangeRequestWorkflowModel::getId, Function.identity()));
    }
}