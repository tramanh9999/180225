package vn.com.mbbank.kanban.mbamt.server.service.impl;

import vn.com.mbbank.kanban.mbamt.server.enums.ChangeStage;
import vn.com.mbbank.kanban.mbamt.server.mapper.ChangeFlowMapper;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeFlowModel;
import vn.com.mbbank.kanban.mbamt.server.repository.ChangeFlowRepository;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeFlowService;
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


