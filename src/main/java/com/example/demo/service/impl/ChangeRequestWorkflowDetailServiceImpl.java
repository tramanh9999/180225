package com.example.demo.service.impl;

import com.example.demo.entity.ChangeRequestWorkflowDetailEntity;
import com.example.demo.mapper.ChangeRequestWorkflowDetailMapper;
import com.example.demo.model.ChangeRequestWorkflowDetailModel;
import com.example.demo.repository.ChangeRequestWorkflowDetailRepository;
import com.example.demo.service.ChangeRequestWorkflowDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChangeRequestWorkflowDetailServiceImpl implements ChangeRequestWorkflowDetailService {

    private final ChangeRequestWorkflowDetailRepository detailRepository;
    private final ChangeRequestWorkflowDetailMapper detailMapper;


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
}