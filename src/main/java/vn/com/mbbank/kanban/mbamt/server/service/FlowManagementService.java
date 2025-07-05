package vn.com.mbbank.kanban.mbamt.server.service;

import vn.com.mbbank.kanban.mbamt.server.enums.ApprovalResultStatus;
import vn.com.mbbank.kanban.mbamt.server.model.FlowEdgeModel;
import vn.com.mbbank.kanban.mbamt.server.model.IndexedChangeFlowDataModel;

import java.util.List;
import java.util.Map;

/**
 * The interface Flow management service.
 */
public interface FlowManagementService {

    /**
     * Create fast lookup edge map map.
     *
     * @param edgesList the edges list
     * @return the map
     */
    Map<String, FlowEdgeModel> createFastLookupEdgeMap(List<FlowEdgeModel> edgesList);

    /**
     * Gets flow data by change flow id.
     *
     * @param changeFlowId the change flow id
     * @return the flow data by change flow id
     */
    IndexedChangeFlowDataModel getFlowDataByChangeFlowId(Long changeFlowId);


    /**
     * Gets or default edge by node handle id.
     *
     * @param currentChangeFlowNodeHandleId the current change flow node handle id
     * @param flowData                      the flow data
     * @return the or default edge by node handle id
     */
    FlowEdgeModel getOrDefaultEdgeByNodeHandleId(String currentChangeFlowNodeHandleId,
                                                 IndexedChangeFlowDataModel flowData);

    String buildHandleOutputIdForApprovalAction(String nodeId, ApprovalResultStatus status);
}
