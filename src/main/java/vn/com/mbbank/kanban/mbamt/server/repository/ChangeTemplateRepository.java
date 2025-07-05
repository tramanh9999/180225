package vn.com.mbbank.kanban.mbamt.server.repository;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeTemplateRepository extends JpaRepository<ChangeTemplateEntity, Long> {
}
