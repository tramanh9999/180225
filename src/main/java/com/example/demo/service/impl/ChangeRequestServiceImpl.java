package com.example.demo.service.impl;

import com.example.demo.entity.entity.ChangeRequestEntity;
import com.example.demo.mapper.ChangeRequestMapper;
import com.example.demo.mapper.ChangeTemplateMapper;
import com.example.demo.model.ChangeRequestModel;
import com.example.demo.repository.ChangeRequestRepository;
import com.example.demo.repository.ChangeRequestService;
import com.example.demo.repository.ChangeTemplateRepository;
import com.example.demo.service.dto.BusinessException;
import com.example.demo.service.dto.ErrorCodeCommon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChangeRequestServiceImpl implements ChangeRequestService {
    private final ChangeRequestRepository changeRequestRepository;
    private final ChangeTemplateRepository changeTemplateRepository;
    private final ChangeRequestMapper changeRequestMapper;
    private final ChangeTemplateMapper changeTemplateMapper;

    @Override
    public ChangeRequestModel findById(Long changeRequestId) throws BusinessException {
        if (changeRequestId == null) {
            throw new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_ID_REQUIRED);
        }
        ChangeRequestEntity changeRequest = changeRequestRepository.findById(changeRequestId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCodeCommon.CHANGE_REQUEST_ID_NOT_FOUND,
                                changeRequestId));
        return changeRequestMapper.toDto(changeRequest);
    }
}