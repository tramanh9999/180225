package com.example.demo.service;

import com.example.demo.dtos.GroupDto;
import com.example.demo.entity.GroupEntity;
import com.example.demo.mapper.GroupMapper;
import com.example.demo.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper = GroupMapper.INSTANCE;

    @Override
    public Page<GroupDto> getChangeRoleGroups(Pageable pageable) {
        Page<GroupEntity> groupEntities = groupRepository.findByIsChangeRoleAndDeletedFalse(true, pageable);
        return groupEntities.map(groupMapper::toDto);
    }
}
