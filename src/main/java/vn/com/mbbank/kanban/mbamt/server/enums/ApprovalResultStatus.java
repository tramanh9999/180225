package vn.com.mbbank.kanban.mbamt.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * The enum Approval result status.
 */
@Getter
@AllArgsConstructor
public enum ApprovalResultStatus {
    ACCEPT("Accept"), REJECT("Reject"), PENDING_APPROVAL("Pending Approval"),
    TOBE_SENT("To Be Sent"), UNKNOWN("Unknown Status");
    private final String value;
    
    public static Optional<ApprovalResultStatus> fromValue(String text) {
        if (text == null) {
            return Optional.empty();
        }
        return Arrays.stream(ApprovalResultStatus.values())
                .filter(status -> status.value.equalsIgnoreCase(
                        text)) // Case-insensitive comparison
                .findFirst();
    }

}