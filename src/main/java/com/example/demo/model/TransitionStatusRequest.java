package com.example.demo.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransitionStatusRequest {
    // The ID of the handle from the current node that the coordinator wishes to activate.
    // Example: "0-output" for a generic stage progression.
    // This dictates the path the workflow should take.
    private String exitingHandleId;
    private String comment; // Optional: Reason for the status change.
    // Optionally, you might include a targetStatusId if the handle doesn't inherently imply it,
    // but the FlowEdgeModel's targetHandle's changeStatusId is preferred for consistency.
}