package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowNode {
    private String id;
    private String type;
    private Position position;
    private NodeData data;
    private boolean deletable;
    private int width;
    private int height;
    private boolean selected;
    private Position positionAbsolute;
    private boolean dragging;

    @Data
    public static class Position {
        private double x;
        private double y;
    }

    @Data
    public static class NodeData {
        private String label;
        private String stageType;
        private List<Status> statusList;
        private String name;
        private String type;
        private String id;
        private String nodeId;
        private List<Group> groups;
        private Integer nodeLevel;
    }

    @Data
    public static class Status {
        private Integer id;
        private String name;
        private String description;
        private String action;
        private String stage;
        private String createdDate;
        private String createdBy;
        private String modifiedBy;
        private String modifiedDate;
    }

    @Data
    public static class Group {
        private String id;
        private String name;
    }
}
