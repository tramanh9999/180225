package vn.com.mbbank.kanban.mbamt.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.com.mbbank.kanban.mbamt.server.entity.ChangeFlowNodeEntity;
import vn.com.mbbank.kanban.mbamt.server.enums.ChangeFlowNodeTypeEnum;
import vn.com.mbbank.kanban.mbamt.server.mapper.ChangeFlowNodeMapper;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeFlowNodeModel;
import vn.com.mbbank.kanban.mbamt.server.model.PagingRequestModel;
import vn.com.mbbank.kanban.mbamt.server.repository.ChangeFlowNodeRepository;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeFlowNodeService;

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
    public List<ChangeFlowNodeModel> findAllChangeFlowNodesByChangeTemplateId(
            Long changeTemplateId) {
        return new ArrayList<>(
                changeFlowNodeRepository.findChangeFlowNodesByTemplateIdAndTypeIn(changeTemplateId,
                        List.of(ChangeFlowNodeTypeEnum.CAB, ChangeFlowNodeTypeEnum.APPROVAL)));
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
        return changeFlowNodeMapper.toModel(entity);
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
        return changeFlowNodeMapper.toModel(entity);
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

        return changeFlowNodeMapper.toModels(entities);

    }
}
