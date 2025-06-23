package com.example.demo.model;

import com.example.demo.enums.HandleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlowEdgeModel {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Pattern NUMERIC_HANDLE_PATTERN =
            Pattern.compile("^(\\d+)-(input|output)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern CUSTOM_NODE_HANDLE_PATTERN =
            Pattern.compile("^(.*?)-(\\d+)-(Accept|Reject)-(output)$", Pattern.CASE_INSENSITIVE);
    private String id;
    private String source;
    private String target;
    private ParsedHandleModel sourceHandle;
    private ParsedHandleModel targetHandle;
    private String type;

    private static ParsedHandleModel parseHandleString(String handleString) {
        if (handleString == null || handleString.isEmpty()) {
            return new ParsedHandleModel(null, HandleType.DEFAULT, null, null);
        }

        ParsedHandleModel parsedHandle = new ParsedHandleModel();
        parsedHandle.setRawHandleId(handleString);
        parsedHandle.setType(HandleType.fromString(handleString));

        Matcher numericMatcher = NUMERIC_HANDLE_PATTERN.matcher(handleString);
        if (numericMatcher.matches()) {
            try {
                parsedHandle.setChangeStatusId(Long.parseLong(numericMatcher.group(1)));
            } catch (NumberFormatException e) {
            }
        } else {
            Matcher customMatcher = CUSTOM_NODE_HANDLE_PATTERN.matcher(handleString);
            if (customMatcher.matches()) {
                parsedHandle.setCustomAction(customMatcher.group(3));
            }
        }

        return parsedHandle;
    }

    public static FlowEdgeModel fromSimpleFlowEdge(FlowEdge simpleEdge) {
        FlowEdgeModel model = new FlowEdgeModel();
        model.setId(simpleEdge.getId());
        model.setSource(simpleEdge.getSource());
        model.setTarget(simpleEdge.getTarget());
        model.setType(simpleEdge.getType());
        model.setSourceHandle(parseHandleString(simpleEdge.getSourceHandle()));
        model.setTargetHandle(parseHandleString(simpleEdge.getTargetHandle()));
        return model;
    }
}