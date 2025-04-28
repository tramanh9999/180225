package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ChangeTemplateModel;
import com.example.demo.dtos.ChangeTemplateFieldItemDto;
import com.example.demo.mapper.ChangeTemplateMapper;
import com.example.demo.mapper.ChangeTemplateFieldItemMapper;
import com.example.demo.repository.ChangeTemplateRepository;
import com.example.demo.entity.ChangeTemplateFieldItemEntity;
import com.example.demo.repository.ChangeTemplateFieldItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ChangeTemplateService.
 */
@Service
public class ChangeTemplateServiceImpl implements ChangeTemplateService {

    @Autowired
    private ChangeTemplateRepository changeTemplateRepository;

    @Autowired
    private ChangeTemplateMapper changeTemplateMapper;

    @Autowired
    private ChangeTemplateFieldItemRepository changeTemplateFieldItemRepository;

    @Autowired
    private ChangeTemplateFieldItemMapper changeTemplateFieldItemMapper;

    /**
     * Finds paginated Change Templates.
     */
    @Override
    public Page<ChangeTemplateModel> findPagings(Object paginationRequest) {
        // This implementation assumes paginationRequest can be converted to a Pageable
        // object.
        // A more robust implementation would handle different types of pagination
        // requests.
        // For now, returning an empty page or throwing an exception is a placeholder.
        // Example: Pageable pageable = ... convert paginationRequest to Pageable
        // return
        // changeTemplateRepository.findAll(pageable).map(changeTemplateMapper::toDto);
        return Page.empty(); // Placeholder implementation
    }

    /**
     * Deletes Change Templates by IDs.
     */
    @Override
    public void deleteByIds(List<Long> ids) {
        changeTemplateRepository.deleteAllById(ids);
    }

    /**
     * Finds a Change Template by ID.
     */
    @Override
    public ChangeTemplateModel findById(Long id) {
        return changeTemplateRepository.findById(id)
                .map(changeTemplateMapper::toDto)
                .orElse(null); // Or throw an exception if not found
    }

    /**
     * Saves a Change Template.
     */
    @Override
    public ChangeTemplateModel save(ChangeTemplateModel changeTemplateModel) {
        // Assuming save is for creating a new entity
        changeTemplateModel.setId(null); // Ensure ID is null for new entity
        return saveInternal(changeTemplateModel);
    }

    /**
     * Saves an existing Change Template by ID.
     */
    @Override
    public ChangeTemplateModel save(Long id, ChangeTemplateModel changeTemplateModel) {
        // Assuming save(id, model) is for updating an existing entity
        changeTemplateModel.setId(id); // Set the ID for update
        return saveInternal(changeTemplateModel);
    }

    private ChangeTemplateModel saveInternal(ChangeTemplateModel changeTemplateModel) {
        // Convert DTO to Entity
        var entity = changeTemplateMapper.toEntity(changeTemplateModel);
        // Save Entity using repository
        var savedEntity = changeTemplateRepository.save(entity);
        // Convert saved Entity back to DTO
        return changeTemplateMapper.toDto(savedEntity);
    }

    /**
     * Gets paginated field item data for a Change Template.
     */
    @Override
    public Page<ChangeTemplateFieldItemDto> getPaginatedFieldItems(Long changeTemplateId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChangeTemplateFieldItemEntity> entityPage = changeTemplateFieldItemRepository
                .findByChangeTemplateId(changeTemplateId, pageable);

        // Map the entity page to a DTO page
        return entityPage.map(changeTemplateFieldItemMapper::toDto);
    }
}
