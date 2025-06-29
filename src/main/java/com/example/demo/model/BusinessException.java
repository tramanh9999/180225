package com.example.demo.model;


import java.text.MessageFormat;
import java.util.Arrays;

public class BusinessException extends RuntimeException {
    private final ErrorCodeCommon errorCode;
    private final Object[] messageArgs;

    public BusinessException(ErrorCodeCommon errorCode, Object... messageArgs) {
        // Crucial: Use String.format to create the actual message
        // Handle potential issues if messageArgs is null or empty, or messageFormat is null
        super(formatMessage(errorCode, messageArgs));
        this.errorCode = errorCode;
        this.messageArgs = messageArgs;
    }

    private static String formatMessage(ErrorCodeCommon errorCode, Object... args) {
        if (errorCode == null) {
            return "Unknown error: ErrorCodeCommon is null.";
        }
        String formatString = errorCode.getMessage();
        if (formatString == null || formatString.isEmpty()) {
            return "Error Code: " + errorCode.getCode() + " (No message format provided)";
        }
        try {
            // Attempt to format the message
            return MessageFormat.format(formatString, args);
        } catch (java.util.MissingFormatArgumentException |
                 java.util.IllegalFormatConversionException e) {
            // Log an internal error if formatting fails due to wrong args
            System.err.println("ERROR: Failed to format BusinessException message for code " +
                    errorCode.getCode() + " with format '" + formatString + "' and args " +
                    Arrays.toString(args) + ". Error: " + e.getMessage());
            return "Error Code: " + errorCode.getCode() + " (Formatting error: " + e.getMessage() +
                    ")";
        }
    }


    public Object[] getMessageArgs() {
        return messageArgs;
    }

    // Override toString for better logging and debugging when BusinessException is printed directly
    @Override
    public String toString() {
        // This is called when the exception itself is printed (e.g., in `with root cause BusinessException: null`)
        // Ensure it provides a comprehensive message.
        return "BusinessException(" + "code=" + (errorCode != null ? errorCode.getCode() : "N/A") +
                ", message='" +
                (this.getMessage() != null ? this.getMessage() : "null message from super") + "'" +
                // Include the arguments for debugging, if needed
                // ", args=" + (messageArgs != null ? Arrays.toString(messageArgs) : "[]") +
                ')';
    }
}