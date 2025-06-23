package com.example.demo.model;

// DTO để trả về điểm đích tiếp theo
public class NextFlowTargetModel {
    private String nextNodeId;
    private String nextTargetHandle; // Đây là handle trên node đích

    public NextFlowTargetModel(String nextNodeId, String nextTargetHandle) {
        this.nextNodeId = nextNodeId;
        this.nextTargetHandle = nextTargetHandle;
    }

    // Getters
    public String getNextNodeId() {
        return nextNodeId;
    }

    public String getNextTargetHandle() {
        return nextTargetHandle;
    }
}