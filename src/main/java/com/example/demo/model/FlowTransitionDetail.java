package com.example.demo.model;

import com.example.demo.entity.ChangeRequestEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlowTransitionDetail {

    private Long changeFlowId;
    private ChangeRequestEntity changeRequest;
    private String currentChangeFlowNodeHandleId;

}