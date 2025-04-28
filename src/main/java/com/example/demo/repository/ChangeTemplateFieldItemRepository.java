package com.example.demo.repository;

import com.example.demo.entity.ChangeTemplateFieldItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ChangeTemplateFieldItemRepository extends JpaRepository<ChangeTemplateFieldItemEntity, Long> {

    /**
     * Finds paginated ChangeTemplateFieldItemEntity by changeTemplateId.
     *
     * @param changeTemplateId The ID of the Change Template.
     * @param pageable         The pagination information.
     * @return A page of ChangeTemplateFieldItemEntity.
     */
    Page<ChangeTemplateFieldItemEntity> findByChangeTemplateId(Long changeTemplateId, Pageable pageable);
}
