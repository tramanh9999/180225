package com.example.demo.service;

import com.example.demo.entity.ChangeFlowEntity;
import com.example.demo.enums.ApprovalResultStatus;
import com.example.demo.model.FlowEdgeModel;
import com.example.demo.model.FlowNodeModel;
import com.example.demo.model.IndexedChangeFlowDataModel;

import java.util.List;
import java.util.Map;

public interface FlowManagementService {

    Map<String, FlowEdgeModel> createFastLookupEdgeMap(List<FlowEdgeModel> edgesList);

    IndexedChangeFlowDataModel buildFromLists(List<FlowNodeModel> nodes,
                                              List<FlowEdgeModel> rawEdges);

    IndexedChangeFlowDataModel getFlowDataByChangeFlowId(Long changeFlowId);

    ChangeFlowEntity saveFlowDataToChangeFlow(Long changeFlowId, List<FlowNodeModel> nodes,
                                              List<FlowEdgeModel> edges);

    FlowEdgeModel getOrDefaultEdgeByNodeHandleId(Long changeFlowId, String currentSourceHandleId,
                                                 Map<String, FlowEdgeModel> indexedEdges);

    String createApprovalNodeHandleIdFromNodeIdAndStatus(String currentApprovalNodeId,
                                                         ApprovalResultStatus status);
}
