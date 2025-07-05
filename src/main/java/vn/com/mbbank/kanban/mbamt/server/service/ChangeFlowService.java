package vn.com.mbbank.kanban.mbamt.server.service;

import vn.com.mbbank.kanban.mbamt.server.enums.ChangeStage;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeFlowModel;

import java.util.Optional;

public interface ChangeFlowService {
    Optional<ChangeFlowModel> findById(Long id);

    String findCurrentNodeIdByChangeFlowIdAndStage(Long changeFlowId, ChangeStage stage);
}
