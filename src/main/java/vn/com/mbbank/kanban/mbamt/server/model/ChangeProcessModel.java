package vn.com.mbbank.kanban.mbamt.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Model representing a change process.
 * This class currently does not contain any fields or methods.
 * It can be extended in the future to include properties related to change processes.
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
public class ChangeProcessModel extends ChangeRequestApprovalResultModel {
    long changeStatusId;
    String changeStatusName;


}
