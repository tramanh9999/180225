package vn.com.mbbank.kanban.mbamt.server.repository;

import vn.com.mbbank.kanban.mbamt.server.entity.CustomFieldValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomFieldValueRepository extends JpaRepository<CustomFieldValueEntity, Long> {
    // Add custom query methods here if needed
}
