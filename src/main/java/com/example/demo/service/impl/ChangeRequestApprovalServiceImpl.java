package com.example.demo.service.impl;

import com.example.demo.entity.ChangeRequestApprovalEntity;
import com.example.demo.mapper.ChangeRequestApprovalMapper;
import com.example.demo.model.ChangeRequestApprovalModel;
import com.example.demo.model.ChangeRequestRoleModel;
import com.example.demo.repository.ChangeRequestApprovalRepository;
import com.example.demo.service.ChangeRequestApprovalResultService;
import com.example.demo.service.ChangeRequestApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChangeRequestApprovalServiceImpl implements ChangeRequestApprovalService {

    @Autowired
    ChangeRequestApprovalResultService approvalResultService;
    @Autowired
    private ChangeRequestApprovalRepository approvalRepository;
    @Autowired
    private ChangeRequestApprovalMapper approvalMapper;

    @Override
    public ChangeRequestApprovalModel findById(Long id) {
        return approvalRepository.findById(id).map(approvalMapper::toModel).orElse(null);
    }

    @Override
    public List<ChangeRequestApprovalModel> findAll() {
        return approvalRepository.findAll().stream().map(approvalMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChangeRequestApprovalModel> saveList(
            List<ChangeRequestApprovalModel> approvalDtos) {
        List<ChangeRequestApprovalEntity> approvals = approvalMapper.toEntity(approvalDtos);
        List<ChangeRequestApprovalEntity> savedApproval = approvalRepository.saveAll(approvals);
        return approvalMapper.toModel(savedApproval);
    }

    @Override
    public void deleteById(Long id) {
        approvalRepository.deleteById(id);
    }

    @Override
    public List<ChangeRequestApprovalModel> findByChangeRequestId(Long changeRequestId) {
        return approvalRepository.findByChangeRequestId(changeRequestId).stream()
                .map(approvalMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public List<ChangeRequestApprovalModel> findByOverallStatus(String status) {
        return approvalRepository.findByOverallStatus(status).stream().map(approvalMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChangeRequestApprovalModel> findByChangeRequestIdAndOverallStatus(
            Long changeRequestId, String status) {
        return approvalRepository.findByChangeRequestIdAndOverallStatus(changeRequestId, status)
                .stream().map(approvalMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public ChangeRequestRoleModel findByChangeFlowIdAndNodeId(Long changeRequestId,
                                                              Long changeFlowId,
                                                              String changeFlowNodeStrId) {
        return null;
    }

    @Override
    public List<ChangeRequestApprovalModel> findByChangeRequestRoleUserIdIn(
            List<Long> roleUserIds) {

        return roleUserIds == null || roleUserIds.isEmpty() ? List.of() :
                // Return an empty list if no role user IDs are provided
                approvalMapper.toModel(
                        approvalRepository.findByChangeRequestRoleUserIdIn(roleUserIds));
    }

    @Override
    public ChangeRequestApprovalModel save(ChangeRequestApprovalModel replyModel) {
        ChangeRequestApprovalEntity savedApproval =
                approvalRepository.save(approvalMapper.toEntity(replyModel));
        return approvalMapper.toModel(savedApproval);
    }

}
