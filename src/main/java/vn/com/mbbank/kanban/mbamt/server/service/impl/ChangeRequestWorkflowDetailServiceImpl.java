package vn.com.mbbank.kanban.mbamt.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestWorkflowDetailEntity;
import vn.com.mbbank.kanban.mbamt.server.mapper.ChangeRequestWorkflowDetailMapper;
import vn.com.mbbank.kanban.mbamt.server.model.*;
import vn.com.mbbank.kanban.mbamt.server.repository.ChangeRequestWorkflowDetailRepository;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeFlowNodeService;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeNodeService;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeRequestWorkflowDetailService;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeRequestWorkflowService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChangeRequestWorkflowDetailServiceImpl implements ChangeRequestWorkflowDetailService {

    private final ChangeRequestWorkflowDetailRepository detailRepository;
    private final ChangeRequestWorkflowDetailMapper detailMapper;
    private final ChangeRequestWorkflowService changeRequestWorkflowService;
    private final ChangeFlowNodeService changeFlowNodeService;
    private final ChangeNodeService changeNodeService;
    private final ChangeRequestWorkflowDetailService changeRequestWorkflowDetailService;


    @Override
    @Transactional(readOnly = true)
    public Optional<ChangeRequestWorkflowDetailModel> getDetailById(Long id) {
        return detailRepository.findById(id).map(detailMapper::toModel); // Map entity to model
    }


    @Transactional(readOnly = true)
    @Override
    public List<ChangeRequestWorkflowDetailModel> getDetailsByIds(List<Long> ids) {
        return detailRepository.findAllById(ids).stream().map(detailMapper::toModel)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<ChangeRequestWorkflowDetailModel> getAllDetailsByWorkflowId(
            Long changeRequestWorkflowId) {

        return detailRepository.findByChangeRequestWorkflowId(changeRequestWorkflowId).stream()
                .map(detailMapper::toModel).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChangeRequestWorkflowDetailModel createDetail(
            ChangeRequestWorkflowDetailModel detailModel) {

        ChangeRequestWorkflowDetailEntity entityToSave = detailMapper.toEntity(detailModel);
        ChangeRequestWorkflowDetailEntity savedEntity = detailRepository.save(entityToSave);
        return detailMapper.toModel(savedEntity); // Map back to model
    }

    @Override
    @Transactional
    public ChangeRequestWorkflowDetailModel updateDetail(
            ChangeRequestWorkflowDetailModel detailModel) {
        if (detailModel.getId() == null) {
            throw new IllegalArgumentException("Workflow Detail ID cannot be null for update.");
        }
        ChangeRequestWorkflowDetailEntity existingEntity =
                detailRepository.findById(detailModel.getId()).orElseThrow(
                        () -> new IllegalArgumentException(
                                "Workflow Detail with ID " + detailModel.getId() +
                                        " not found for update."));
        // Update existing entity with data from model
        detailMapper.updateEntityFromModel(detailModel, existingEntity);
        ChangeRequestWorkflowDetailEntity updatedEntity = detailRepository.save(existingEntity);
        return detailMapper.toModel(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteDetail(Long id) {
        if (!detailRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Workflow Detail with ID " + id + " not found for deletion.");
        }
        detailRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChangeRequestWorkflowDetailModel> getDetailByWorkflowIdAndChangeNodeId(
            Long changeRequestWorkflowId, Long changeNodeId) {
        return detailRepository.findByChangeRequestWorkflowIdAndChangeNodeId(
                        changeRequestWorkflowId, changeNodeId)
                .map(detailMapper::toModel); // Map entity to model
    }

    @Override
    public List<ChangeRequestWorkflowDetailModel> findWorkflowDetailByIds(
            List<Long> changeRequestWorkflowDetailIds) {
        var workflowDetails = detailRepository.findByIdIn(changeRequestWorkflowDetailIds).stream()
                .map(detailMapper::toModel).collect(Collectors.toList());

        //filter not null workflow id
        var workflowIds = workflowDetails.stream()
                .map(ChangeRequestWorkflowDetailModel::getChangeRequestWorkflowId)
                .filter(Objects::nonNull).toList();

        var changeNodeIds =
                workflowDetails.stream().map(ChangeRequestWorkflowDetailModel::getChangeNodeId)
                        .filter(Objects::nonNull).toList();

        //convert to map
        var workflowMaps = changeRequestWorkflowService.getMapChangeWorkflowByIds(workflowIds);

        var changeNodeMaps = changeNodeService.findAllByIds(changeNodeIds).stream()
                .collect(Collectors.toMap(ChangeWorkflowNodeModel::getId, Function.identity()));

        // set    private ChangeNodeModel changeNodeModel;
        //    private ChangeRequestWorkflowModel changeRequestWorkflowModel;  to workflowDetails
        workflowDetails.forEach(detail -> {

            // if id null or not found, throw exception
            if (detail.getChangeRequestWorkflowId() == null) {
                // throw BusinessException
                throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_WORKFLOW_ID_REQUIRED);
            }
            if (workflowMaps.get(detail.getChangeRequestWorkflowId()) == null) {
                throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_WORKFLOW_ID_REQUIRED);
            }
            // same with change node
            if (detail.getChangeNodeId() == null) {
                // throw BusinessException
                throw new BusinessException(ErrorCodeCommon.CHANGE_NODE_ID_REQUIRED);
            }
            if (changeNodeMaps.get(detail.getChangeNodeId()) == null) {
                throw new BusinessException(ErrorCodeCommon.CHANGE_NODE_ID_REQUIRED);
            }

            detail.setChangeRequestWorkflowModel(
                    workflowMaps.get(detail.getChangeRequestWorkflowId()));
            detail.setChangeNodeModel(changeNodeMaps.get(detail.getChangeNodeId()));
        });

        return workflowDetails;
    }

    @Override
    public List<ChangeRequestWorkflowDetailModel> findAllByChangeRequestId(Long changeRequestId) {
        List<Long> wlIds =
                changeRequestWorkflowService.findAllByChangeRequestId(changeRequestId).stream()
                        .map(ChangeRequestWorkflowModel::getId).collect(Collectors.toList());
        return changeRequestWorkflowDetailService.findAllByChangeRequestWorkflowId(wlIds).stream()
                .map(detailMapper::toModel).toList();
    }
}