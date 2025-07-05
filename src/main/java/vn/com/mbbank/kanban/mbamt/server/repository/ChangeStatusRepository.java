package vn.com.mbbank.kanban.mbamt.server.repository;

import vn.com.mbbank.kanban.mbamt.server.entity.ChangeStatusEntity;
import vn.com.mbbank.kanban.mbamt.server.enums.ChangeStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChangeStatusRepository extends JpaRepository<ChangeStatusEntity, Long> {

    /**
     * Finds a ChangeStatusEntity by its unique name.
     *
     * @param name The name of the change status.
     * @return An Optional containing the found entity, or empty if not found.
     */
    Optional<ChangeStatusEntity> findByName(String name);

    /**
     * Finds all ChangeStatus entities belonging to a specific stage.
     *
     * @param stage The stage name.
     * @return A list of ChangeStatusEntity for the given stage.
     */
    List<ChangeStatusEntity> findByStage(ChangeStage stage);
}