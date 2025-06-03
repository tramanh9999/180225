package com.example.demo.service;

import com.example.demo.model.PagingRequestModel;
import com.example.demo.model.SysGroupModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupService {
    Page<SysGroupModel> getChangeRoleGroups(Pageable pageable);

    Page<SysGroupModel> getGroups(PagingRequestModel request);
}
