package vn.com.mbbank.kanban.mbamt.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestWorkflowEntity;
import vn.com.mbbank.kanban.mbamt.server.mapper.ChangeRequestWorkflowMapper;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestWorkflowModel;
import vn.com.mbbank.kanban.mbamt.server.repository.ChangeRequestWorkflowRepository;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeRequestWorkflowService;

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
    public Map<Long, ChangeRequestWorkflowModel> getMapChangeWorkflowByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        List<ChangeRequestWorkflowEntity> entities =
                changeRequestWorkflowRepository.findByIdIn(ids);
        List<ChangeRequestWorkflowModel> models = changeRequestWorkflowMapper.toDtoList(entities);
        return models.stream()
                .collect(Collectors.toMap(ChangeRequestWorkflowModel::getId, Function.identity()));
    }

    /**
     * Retrieves a workflow by its ID.
     *
     * @param id The ID of the workflow.
     * @return An Optional containing the workflow if found, or empty otherwise.
     */
    @Transactional(readOnly = true)
    @Override
    public ChangeRequestWorkflowModel getWorkflowById(Long id) {
        return changeRequestWorkflowMapper.toDto(
                changeRequestWorkflowRepository.findById(id).get());
    }

    @Override
    public List<ChangeRequestWorkflowModel> findAllByChangeRequestId(Long changeRequestId) {
        return changeRequestWorkflowRepository.findAllByChangeId(changeRequestId).stream()
                .map(changeRequestWorkflowMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves all workflows.
     *
     * @return A list of all workflows.
     */
    @Transactional(readOnly = true)
    public List<ChangeRequestWorkflowEntity> getAllWorkflows() {
        return changeRequestWorkflowRepository.findAll();
    }


    /**
     * Deletes a workflow by its ID.
     *
     * @param id The ID of the workflow to delete.
     * @throws IllegalArgumentException if the workflow with the given ID does not exist.
     */
    @Transactional
    public void deleteWorkflow(Long id) {
        if (!changeRequestWorkflowRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Workflow with ID " + id + " not found for deletion.");
        }
        changeRequestWorkflowRepository.deleteById(id);
    }

}



