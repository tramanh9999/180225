package vn.com.mbbank.kanban.mbamt.server.service;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestHistoryEntity;
import vn.com.mbbank.kanban.mbamt.server.mapper.ChangeRequestHistoryMapper;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestHistoryModel;
import vn.com.mbbank.kanban.mbamt.server.repository.ChangeRequestHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service implementation for managing Change Request history records.
 * This service handles the creation of history records, including the retrieval
 * of old and new change status details to populate stage information.
 */
@Service
@RequiredArgsConstructor
public class ChangeRequestHistoryServiceImpl implements ChangeRequestHistoryService {

    private final ChangeRequestHistoryRepository historyRepository;
    private final ChangeRequestHistoryMapper historyMapper;
    private final ChangeStatusService changeStatusService;
    // Inject ChangeStatusService to get stage info

    /**
     * Creates a new history record for a Change Request, including stage information.
     *
     * @param historyModel The ChangeRequestHistoryModel to save.
     * @return The saved ChangeRequestHistoryModel.
     */

    @Transactional
    @Override
    public ChangeRequestHistoryModel save(ChangeRequestHistoryModel historyModel) {
        if (historyModel.getChangeRequestId() == null) {
            throw new IllegalArgumentException(
                    "Change Request ID cannot be null for history record.");
        }
        ChangeRequestHistoryEntity entityToSave = historyMapper.toEntity(historyModel);
        ChangeRequestHistoryEntity savedEntity = historyRepository.save(entityToSave);
        return historyMapper.toModel(savedEntity);
    }

    // ... (other methods of ChangeRequestHistoryServiceImpl remain the same) ...
}