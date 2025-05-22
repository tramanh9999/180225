package com.example.demo.service;

import com.example.demo.entity.GroupEntity;
import com.example.demo.mapper.GroupMapper;
import com.example.demo.model.GroupModel;
import com.example.demo.model.PagingRequestModel;
import com.example.demo.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper = GroupMapper.INSTANCE;

    @Override
    public Page<GroupModel> getChangeRoleGroups(Pageable pageable) {
        Page<GroupEntity> groupEntities = groupRepository.findByIsChangeRoleAndDeletedFalse(true, pageable);
        return groupEntities.map(groupMapper::toDto);
    }

    @Override
    public Page<GroupModel> getGroups(PagingRequestModel request) {
        Sort sort = Sort.by(request.isReverse() ? Sort.Direction.DESC : Sort.Direction.ASC,
                request.getSortBy() != null ? request.getSortBy() : "id");
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<GroupEntity> groupEntities = groupRepository.findAll(pageRequest);
        return groupEntities.map(groupMapper::toDto);
    }
}
