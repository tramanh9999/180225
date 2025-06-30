package com.example.demo.model;

import com.example.demo.enums.ApprovalResultStatus;
import com.example.demo.enums.FlowConstants;
import com.example.demo.enums.HandleType;
import com.example.demo.enums.NodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;

import static com.example.demo.enums.FlowConstants.*;

/**
 * Model representing an edge in a flow graph.
 * It includes the source and target nodes, as well as parsed handles for both ends.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class FlowEdgeModel {

    private String id;
    private String sourceNodeId;
    private String targetNodeId;
    private ParsedNodeHandleModel sourceHandle;
    private ParsedNodeHandleModel targetHandle;

    private FlowNodeModel sourceNode;
    private FlowNodeModel targetNode;

    private Long changeFlowId;
    private ApprovalResultStatus resultStatus;


    public static ParsedNodeHandleModel parseHandleString(String handleString) {
        if (handleString == null || handleString.isEmpty()) {
            return new ParsedNodeHandleModel(null, null, null, null, ApprovalResultStatus.UNKNOWN);
        }

        ParsedNodeHandleModel parsedHandle = new ParsedNodeHandleModel();
        parsedHandle.setRawHandleId(handleString);
        parsedHandle.setType(HandleType.fromHandleIdString(handleString));

        Matcher numericMatcher = CHANGE_STATUS_ID_HANDLE_PATTERN.matcher(handleString);
        Matcher customApprovalMatcher = NODE_APPROVAL_HANDLE_PATTERN.matcher(handleString);
        Matcher nodeMatcher = NODE_ID_HANDLE_PATTERN.matcher(handleString);

        if (numericMatcher.matches()) {
            try {
                parsedHandle.setChangeStatusId(Long.parseLong(numericMatcher.group(
                        CHANGE_STATUS_ID_HANDLE_PATTERN__CHANGE_STATUS_ID_INDEX)));
            } catch (NumberFormatException e) {
                log.error("Failed to parse change status ID from numeric handle: '{}'. Error: {}",
                        handleString, e.getMessage());
            }
        } else if (customApprovalMatcher.matches()) {
            String nodeIdPart =
                    customApprovalMatcher.group(NODE_APPROVAL_HANDLE_PATTERN__NODE_ID_INDEX);
            parsedHandle.setNodeId(nodeIdPart);
            parsedHandle.setCustomAction(ApprovalResultStatus.fromValue(customApprovalMatcher.group(
                            NODE_APPROVAL_HANDLE_PATTERN__NODE_APPROVAL_ACTION_INDEX))
                    .orElse(ApprovalResultStatus.UNKNOWN));
        } else if (nodeMatcher.matches()) {
            String nodeIdPart = nodeMatcher.group(NODE_ID_HANDLE_PATTERN__NODE_ID_INDEX);
            parsedHandle.setNodeId(nodeIdPart);
            parsedHandle.setType(nodeMatcher.group(NODE_ID_HANDLE_PATTERN__HANDLE_TYPE_INDEX)
                    .equalsIgnoreCase(FlowConstants.INPUT_KEYWORD) ? HandleType.INPUT :
                    HandleType.OUTPUT);
        } else {
            log.debug("Handle string '{}' did not match any known patterns. " +
                    "Type remains as determined by fromHandleIdString or defaulted.", handleString);
            throw new BusinessException(ErrorCodeCommon.INVALID_HANDLE_FORMAT, handleString);
        }

        return parsedHandle;
    }

    /**
     * Converts a simple FlowEdgeRawModel to a FlowEdgeModel.
     * This method is used to convert raw edge data into a model that includes parsed handles.
     *
     * @param simpleEdge the raw edge data to convert
     * @return a FlowEdgeModel with parsed handles
     */
    public static FlowEdgeModel fromSimpleFlowEdge(FlowEdgeRawModel simpleEdge) {
        FlowEdgeModel model = new FlowEdgeModel();
        model.setId(simpleEdge.getId());
        model.setSourceNodeId(simpleEdge.getSource());
        model.setTargetNodeId(simpleEdge.getTarget());
        model.setSourceHandle(parseHandleString(simpleEdge.getSourceHandle()));
        model.setTargetHandle(parseHandleString(simpleEdge.getTargetHandle()));
        model.setSourceNode(FlowNodeModel.builder().id(simpleEdge.getSource())
                .type(NodeType.parseTypeFromNodeId(simpleEdge.getSource())).build());
        model.setTargetNode(FlowNodeModel.builder().id(simpleEdge.getTarget())
                .type(NodeType.parseTypeFromNodeId(model.getTargetNodeId())).build());

        return model;
    }
}