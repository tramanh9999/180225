package vn.com.mbbank.kanban.mbamt.server.service;

import org.springframework.transaction.annotation.Transactional;
import vn.com.mbbank.kanban.mbamt.server.entity.ChangeRequestWorkflowDetailEntity;
import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestWorkflowDetailModel;

import java.util.List;
import java.util.Optional;

public interface ChangeRequestWorkflowDetailService {

    Optional<ChangeRequestWorkflowDetailModel> getDetailById(Long id);

    @Transactional(readOnly = true)
    List<ChangeRequestWorkflowDetailModel> getDetailsByIds(List<Long> ids);

    List<ChangeRequestWorkflowDetailModel> getAllDetailsByWorkflowId(Long changeRequestWorkflowId);

    ChangeRequestWorkflowDetailModel createDetail(ChangeRequestWorkflowDetailModel detailModel);

    ChangeRequestWorkflowDetailModel updateDetail(ChangeRequestWorkflowDetailModel detailModel);

    void deleteDetail(Long id);

    Optional<ChangeRequestWorkflowDetailModel> getDetailByWorkflowIdAndChangeNodeId(
            Long changeRequestWorkflowId, Long changeNodeId);

    /**
     * [EN] Description: Retrieves a list of ChangeRequestWorkflowDetailModel objects whose IDs are present in the provided list of IDs.
     * <p>
     * [EN] Parameter changeRequestWorkflowDetailIds: A list of Long values representing the IDs of the ChangeRequestWorkflowDetailModel objects to retrieve.
     * This list cannot be null, and should contain valid Long values representing existing ChangeRequestWorkflowDetailModel IDs.
     * An empty list will result in an empty list being returned. If any ID in the list does not correspond to an existing ChangeRequestWorkflowDetailModel, that ID will be ignored, and no exception will be thrown.
     * <p>
     * [EN] Returns: A List of ChangeRequestWorkflowDetailModel objects. Each object in the list corresponds to a ChangeRequestWorkflowDetailModel
     * whose ID was present in the input `changeRequestWorkflowDetailIds` list. The order of the returned list is not guaranteed to match the order
     * of IDs in the input list. If no ChangeRequestWorkflowDetailModel objects are found with the given IDs, an empty list is returned.
     */
    /**
     * [EN] Description: Retrieves a list of ChangeRequestWorkflowDetailModel objects whose IDs are present in the provided list of IDs.
     * <p>
     * [EN] Parameter changeRequestWorkflowDetailIds: A list of Long values representing the IDs of the ChangeRequestWorkflowDetailModel objects to retrieve.
     * This list cannot be null, and should contain valid Long values representing existing ChangeRequestWorkflowDetailModel IDs.
     * An empty list will result in an empty list being returned.
     * <p>
     * [EN] Returns: A List of ChangeRequestWorkflowDetailModel objects. Each object in the list corresponds to a ChangeRequestWorkflowDetailModel
     * whose ID was present in the input `changeRequestWorkflowDetailIds` list. The order of the returned list is not guaranteed to match the order
     * of IDs in the input list. If no ChangeRequestWorkflowDetailModel objects are found with the given IDs, an empty list is returned.
     */
    List<ChangeRequestWorkflowDetailModel> findWorkflowDetailByIds(
            List<Long> changeRequestWorkflowDetailIds);

    /**
     * [EN] Description: Retrieves all ChangeRequestWorkflowDetailModel
     * objects associated with a specific Change Request ID.
     *
     * @param changeRequestId The ID of the Change Request for which to retrieve workflow details.
     * @return A List of ChangeRequestWorkflowDetailModel objects associated with the specified Change Request ID.
     */
    List<ChangeRequestWorkflowDetailModel> findAllByChangeRequestId(Long changeRequestId);

    List<ChangeRequestWorkflowDetailEntity> findAllByChangeRequestWorkflowId(
            List<Long> workflowIds);
}