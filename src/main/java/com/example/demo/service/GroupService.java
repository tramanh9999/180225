package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.GroupModel;
import com.example.demo.model.PagingRequestModel;

public interface GroupService {
    Page<GroupModel> getChangeRoleGroups(Pageable pageable);

    Page<GroupModel> getGroups(PagingRequestModel request);
}
