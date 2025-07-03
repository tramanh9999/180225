package com.example.demo.service;

import com.example.demo.mapper.ChangeNodeMapper;
import com.example.demo.model.ChangeNodeModel;
import com.example.demo.repository.ChangeNodeRepository;
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
