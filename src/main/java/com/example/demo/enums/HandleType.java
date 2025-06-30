package com.example.demo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.example.demo.enums.FlowConstants.*;

/**
 * Enum representing the type of handle in a workflow system.
 * It distinguishes between input and output handles, and provides methods
 * to convert handle ID strings to their corresponding HandleType.
 */
@Getter
@RequiredArgsConstructor
@Slf4j
public enum HandleType {

    INPUT(HANDLE_SEPARATOR + INPUT_KEYWORD), OUTPUT(HANDLE_SEPARATOR + OUTPUT_KEYWORD),
    UNKNOWN(null);

    private final String suffix;


    /**
     * Convert a handle ID string to its corresponding HandleType.
     * If the string is null or empty, it returns DEFAULT.
     *
     * @param handleIdString the handle ID string to convert
     *                       (e.g., "123-input", "456-output", "789")
     *                       or "APPROVAL_NODE-123-Accept-output", "0-output")
     *                       where the suffix indicates the type.
     *                       If the string does not end with a recognized suffix,
     *                       it defaults to DEFAULT.
     * @return the corresponding HandleType based on the suffix
     * or DEFAULT if the string is null, empty, or does not match any known suffix.
     */
    public static HandleType fromHandleIdString(String handleIdString) {
        if (handleIdString == null || handleIdString.isEmpty()) {
            return UNKNOWN;
        }

        if (handleIdString.endsWith(OUTPUT.getSuffix())) {
            return OUTPUT;
        }
        if (handleIdString.endsWith(INPUT.getSuffix())) {
            return INPUT;
        }

        return UNKNOWN;
    }


    /**
     * Returns the keyword associated with this HandleType.
     * For INPUT, it returns "input"; for OUTPUT, it returns "output".
     * If the type is DEFAULT, it returns null.
     *
     * @return the keyword for this HandleType, or null if it's DEFAULT
     */
    public String getKeyword() {
        if (this == INPUT) {
            return INPUT_KEYWORD;
        }
        if (this == OUTPUT) {
            return OUTPUT_KEYWORD;
        }
        return null;
    }
}