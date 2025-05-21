package com.example.demo.service;

import com.example.demo.dtos.GroupDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupService {
    Page<GroupDto> getChangeRoleGroups(Pageable pageable);
}
