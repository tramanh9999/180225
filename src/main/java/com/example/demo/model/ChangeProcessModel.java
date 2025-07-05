package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Model representing a change process.
 * This class currently does not contain any fields or methods.
 * It can be extended in the future to include properties related to change processes.
 */
@Builder
@Data
@AllArgsConstructor
public class ChangeProcessModel extends ChangeRequestApprovalResultModel {
    long changeStatusId;
    String changeStatusName;


}
