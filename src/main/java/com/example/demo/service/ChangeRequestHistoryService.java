package com.example.demo.service;

import com.example.demo.model.ChangeRequestHistoryModel;
import org.springframework.transaction.annotation.Transactional;

public interface ChangeRequestHistoryService {


    @Transactional
    ChangeRequestHistoryModel createHistoryRecord(ChangeRequestHistoryModel historyModel);
}
