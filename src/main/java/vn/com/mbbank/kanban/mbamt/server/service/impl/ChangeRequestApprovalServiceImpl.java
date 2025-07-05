package vn.com.mbbank.kanban.mbamt.server.service.impl;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestApprovalEntity;
import vn.com.mbbank.kanban.mbamt.server.mapper.ChangeRequestApprovalMapper;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestApprovalModel;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestRoleModel;
import vn.com.mbbank.kanban.mbamt.server.repository.ChangeRequestApprovalRepository;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeRequestApprovalResultService;
import vn.com.mbbank.kanban.mbamt.server.service.ChangeRequestApprovalService;
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
