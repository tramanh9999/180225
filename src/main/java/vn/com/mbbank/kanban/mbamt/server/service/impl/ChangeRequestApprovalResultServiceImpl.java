package vn.com.mbbank.kanban.mbamt.server.service.impl;// Trong file ChangeRequestApprovalResultServiceImpl.java (Implementation)

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestApprovalResultEntity;
import vn.com.mbbank.kanban.mbamt.server.mapper.ChangeApprovalUserResultMapper;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestApprovalResultModel;
import vn.com.mbbank.kanban.mbamt.server.repository.ChangeRequestApprovalResultRepository;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeRequestApprovalResultService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ChangeRequestApprovalResultServiceImpl implements ChangeRequestApprovalResultService {

    private final ChangeRequestApprovalResultRepository resultRepository;
    private final ChangeApprovalUserResultMapper resultMapper;

    public ChangeRequestApprovalResultServiceImpl(
            ChangeRequestApprovalResultRepository resultRepository,
            ChangeApprovalUserResultMapper resultMapper) {
        this.resultRepository = resultRepository;
        this.resultMapper = resultMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChangeRequestApprovalResultModel> getResultById(Long id) {
        return resultRepository.findById(id).map(resultMapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChangeRequestApprovalResultModel> getResultsByApprovalId(
            Long changeRequestApprovalId) {
        return resultRepository.findByChangeRequestApprovalId(changeRequestApprovalId).stream()
                .map(resultMapper::toModel).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChangeRequestApprovalResultModel createResult(
            ChangeRequestApprovalResultModel resultModel) {
        // Add validation: e.g., resultModel.getChangeRequestApprovalId() must not be null
        ChangeRequestApprovalResultEntity entityToSave = resultMapper.toEntity(resultModel);
        ChangeRequestApprovalResultEntity savedEntity = resultRepository.save(entityToSave);
        return resultMapper.toModel(savedEntity);
    }

    @Override
    @Transactional
    public ChangeRequestApprovalResultModel updateResult(
            ChangeRequestApprovalResultModel resultModel) {
        // Add validation and existence checks
        ChangeRequestApprovalResultEntity existingEntity =
                resultRepository.findById(resultModel.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Result not found."));
        resultMapper.updateEntityFromModel(resultModel, existingEntity);
        ChangeRequestApprovalResultEntity updatedEntity = resultRepository.save(existingEntity);
        return resultMapper.toModel(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteResult(Long id) {
        // Add existence checks
        resultRepository.deleteById(id);
    }
}