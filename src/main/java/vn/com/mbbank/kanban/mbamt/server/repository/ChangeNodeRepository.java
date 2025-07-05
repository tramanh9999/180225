package vn.com.mbbank.kanban.mbamt.server.repository;

// implement repo with change node entity

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeNodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangeNodeRepository extends JpaRepository<ChangeNodeEntity, Long> {

    List<ChangeNodeEntity> findAllByIdIn(List<Long> ids);

}
