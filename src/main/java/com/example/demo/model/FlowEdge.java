package com.example.demo.model;

import java.util.Map;

public class FlowEdge {
    private String id;
    private String source;
    private String target;
    private String sourceHandle;
    private String targetHandle;
    private String type;
    private boolean deletable;
    private Map<String, Object> markerEnd;
    private Map<String, Object> style;

    // Default constructor
    public FlowEdge() {
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSourceHandle() {
        return sourceHandle;
    }

    public void setSourceHandle(String sourceHandle) {
        this.sourceHandle = sourceHandle;
    }

    public String getTargetHandle() {
        return targetHandle;
    }

    public void setTargetHandle(String targetHandle) {
        this.targetHandle = targetHandle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public Map<String, Object> getMarkerEnd() {
        return markerEnd;
    }

    public void setMarkerEnd(Map<String, Object> markerEnd) {
        this.markerEnd = markerEnd;
    }

    public Map<String, Object> getStyle() {
        return style;
    }

    public void setStyle(Map<String, Object> style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return "FlowEdge{" + "id='" + id + '\'' + ", source='" + source + '\'' + ", target='" +
                target + '\'' + '}';
    }
}
