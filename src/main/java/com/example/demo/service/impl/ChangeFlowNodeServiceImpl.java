package com.example.demo.service.impl;

import com.example.demo.entity.ChangeFlowNodeEntity;
import com.example.demo.mapper.ChangeFlowNodeMapper;
import com.example.demo.model.ChangeFlowNodeModel;
import com.example.demo.model.PagingRequestModel;
import com.example.demo.repository.ChangeFlowNodeRepository;
import com.example.demo.service.ChangeFlowNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChangeFlowNodeServiceImpl implements ChangeFlowNodeService {

    private final ChangeFlowNodeRepository changeFlowNodeRepository;
    private final ChangeFlowNodeMapper changeFlowNodeMapper;

    @Override
    public Map<Long, ChangeFlowNodeModel> findByIdIn(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }

        List<ChangeFlowNodeEntity> entities = changeFlowNodeRepository.findByIdIn(ids);
        List<ChangeFlowNodeModel> models = changeFlowNodeMapper.toDtoList(entities);

        return models.stream()
                .collect(Collectors.toMap(ChangeFlowNodeModel::getId, Function.identity()));
    }

    @Override
    public Page<String> findPagingUsernameById(Long id, PagingRequestModel pagingRequestModel) {
        return changeFlowNodeRepository.findUsernamesByChangeFlowNodeId(id, pagingRequestModel);
    }


    @Override
    public List<ChangeFlowNodeModel> findChangeFlowNodesByTemplateId(Long changeTemplateId) {
        return new ArrayList<>(
                changeFlowNodeRepository.findChangeFlowNodesByTemplateId(changeTemplateId));
    }

    @Override
    public ChangeFlowNodeModel findByNodeId(String nodeId) {

        // Validate input
        if (nodeId == null || nodeId.isEmpty()) {
            return null; // or throw an exception if preferred
        }
        ChangeFlowNodeEntity entity = changeFlowNodeRepository.findByNodeId(nodeId);
        if (entity == null) {
            return null; // or throw an exception if preferred
        }
        return changeFlowNodeMapper.toDto(entity);
    }

    @Override
    public ChangeFlowNodeModel findById(Long changeFlowNodeNodeId) {
        // Validate input
        if (changeFlowNodeNodeId == null) {
            return null; // or throw an exception if preferred
        }
        ChangeFlowNodeEntity entity =
                changeFlowNodeRepository.findById(changeFlowNodeNodeId).orElse(null);
        if (entity == null) {
            return null; // or throw an exception if preferred
        }
        return changeFlowNodeMapper.toDto(entity);
    }

    @Override
    public List<ChangeFlowNodeModel> findByChangeFlowId(Long changeFlowId) {

        // Validate input
        if (changeFlowId == null) {
            return List.of(); // or throw an exception if preferred
        }

        List<ChangeFlowNodeEntity> entities =
                changeFlowNodeRepository.findByChangeFlowId(changeFlowId);
        if (entities == null || entities.isEmpty()) {
            return List.of(); // or throw an exception if preferred
        }

        return changeFlowNodeMapper.toDtoList(entities);

    }
}
