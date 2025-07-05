package vn.com.mbbank.kanban.mbamt.server.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.mbbank.kanban.mbamt.server.enums.ChangeFlowNodeTypeEnum;
import vn.com.mbbank.kanban.mbamt.server.enums.NodeType;

/**
 * Model representing a change flow node.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangeFlowNodeModel {
    private Long id;
    private Long changeFlowId;
    private ChangeFlowNodeTypeEnum type;
    private Integer nodeLevel;
    private String name;
    private String nodeId;

    @Builder.Default
    private NodeType parsedType = NodeType.UNKNOWN;

    @Builder.Default
    private ParsedNodeHandleModel parsedHandle = new ParsedNodeHandleModel();
}
