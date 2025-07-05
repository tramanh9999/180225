package vn.com.mbbank.kanban.mbamt.server.service;

import vn.com.mbbank.kanban.mbamt.server.mapper.ChangeNodeMapper;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeNodeModel;
import vn.com.mbbank.kanban.mbamt.server.repository.ChangeNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChangeNodeServiceImpl implements ChangeNodeService {

    private final ChangeNodeRepository changeNodeRepository;

    private final ChangeNodeMapper changeNodeMapper;

    @Override
    public List<ChangeNodeModel> findAllByIds(List<Long> ids) {
        return changeNodeRepository.findAllByIdIn(ids).stream().map(changeNodeMapper::toModel)
                .toList();
    }
}
