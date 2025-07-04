package com.example.demo.service;

import com.example.demo.entity.ChangeFlowEntity;
import com.example.demo.enums.ApprovalResultStatus;
import com.example.demo.model.FlowEdgeModel;
import com.example.demo.model.FlowNodeModel;
import com.example.demo.model.IndexedChangeFlowDataModel;
import com.example.demo.service.impl.FlowManagementServiceImpl;
import org.springframework.cache.annotation.CacheEvict;

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


    @CacheEvict(value = FlowManagementServiceImpl.FLOW_DATA_CACHE,
            key = "'CHANGE_FLOW_' + #changeFlowId")
    ChangeFlowEntity saveFlowDataToChangeFlow(Long changeFlowId, List<FlowNodeModel> nodes,
                                              List<FlowEdgeModel> edges);

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
