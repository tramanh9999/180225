package com.example.demo.repository;

import com.example.demo.entity.ChangeTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeTemplateRepository extends JpaRepository<ChangeTemplateEntity, Long> {
}
