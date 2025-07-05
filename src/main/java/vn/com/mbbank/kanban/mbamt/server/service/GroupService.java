package vn.com.mbbank.kanban.mbamt.server.service;

import vn.com.mbbank.kanban.mbamt.server.model.PagingRequestModel;
import vn.com.mbbank.kanban.mbamt.server.model.SysGroupModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupService {
    Page<SysGroupModel> getChangeRoleGroups(Pageable pageable);

    Page<SysGroupModel> getGroups(PagingRequestModel request);
}
