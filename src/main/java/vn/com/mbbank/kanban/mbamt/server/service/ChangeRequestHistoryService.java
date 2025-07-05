package vn.com.mbbank.kanban.mbamt.server.service;

import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestHistoryModel;
import org.springframework.transaction.annotation.Transactional;

public interface ChangeRequestHistoryService {


    @Transactional
    ChangeRequestHistoryModel save(ChangeRequestHistoryModel historyModel);
}
