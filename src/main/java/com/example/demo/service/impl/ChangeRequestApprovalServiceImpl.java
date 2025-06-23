package com.example.demo.service.impl;

import com.example.demo.entity.ChangeRequestApprovalEntity;
import com.example.demo.model.ChangeRequestApprovalModel;
import com.example.demo.repository.ChangeRequestApprovalRepository;
import com.example.demo.service.ChangeRequestApprovalService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChangeRequestApprovalServiceImpl implements ChangeRequestApprovalService {

    @Autowired
    private ChangeRequestApprovalRepository approvalRepository;

    @Override
    public ChangeRequestApprovalModel findById(Long id) {
        return approvalRepository.findById(id).map(this::convertToDto).orElse(null);
    }

    @Override
    public List<ChangeRequestApprovalModel> findAll() {
        return approvalRepository.findAll().stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChangeRequestApprovalModel save(ChangeRequestApprovalModel approvalDto) {
        ChangeRequestApprovalEntity approval = convertToEntity(approvalDto);

        // Set created/modified dates if new entity
        if (approval.getId() == null) {
            approval.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        approval.setModifiedDate(Timestamp.valueOf(LocalDateTime.now()));

        ChangeRequestApprovalEntity savedApproval = approvalRepository.save(approval);
        return convertToDto(savedApproval);
    }

    @Override
    public void deleteById(Long id) {
        approvalRepository.deleteById(id);
    }

    @Override
    public List<ChangeRequestApprovalModel> findByChangeRequestId(Long changeRequestId) {
        return approvalRepository.findByChangeRequestId(changeRequestId).stream()
                .map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<ChangeRequestApprovalModel> findByOverallStatus(String status) {
        return approvalRepository.findByOverallStatus(status).stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChangeRequestApprovalModel> findByChangeRequestIdAndOverallStatus(
            Long changeRequestId, String status) {
        return approvalRepository.findByChangeRequestIdAndOverallStatus(changeRequestId, status)
                .stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private ChangeRequestApprovalModel convertToDto(ChangeRequestApprovalEntity approval) {
        ChangeRequestApprovalModel approvalDto = new ChangeRequestApprovalModel();
        BeanUtils.copyProperties(approval, approvalDto);
        return approvalDto;
    }

    private ChangeRequestApprovalEntity convertToEntity(ChangeRequestApprovalModel approvalDto) {
        ChangeRequestApprovalEntity approval = new ChangeRequestApprovalEntity();
        BeanUtils.copyProperties(approvalDto, approval);
        return approval;
    }
}
