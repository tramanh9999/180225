package com.example.demo.repository;

import com.example.demo.entity.entity.ChangeRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * The interface Change request repository.
 */
@Repository
public interface ChangeRequestRepository extends JpaRepository<ChangeRequestEntity, Long> {
}
