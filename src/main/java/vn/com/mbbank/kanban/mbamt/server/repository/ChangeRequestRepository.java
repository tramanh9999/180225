package vn.com.mbbank.kanban.mbamt.server.repository;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * The interface Change request repository.
 */
@Repository
public interface ChangeRequestRepository extends JpaRepository<ChangeRequestEntity, Long> {
}
