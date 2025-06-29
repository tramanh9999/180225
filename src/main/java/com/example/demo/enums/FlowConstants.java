package com.example.demo.enums;

import java.util.regex.Pattern;

public class FlowConstants {
    public static final String HANDLE_SEPARATOR = "-";
    public static final String INPUT_KEYWORD = "input";
    public static final String OUTPUT_KEYWORD = "output";

    /**
     * Regular expression pattern to match numeric handles.
     * It captures the numeric ID and the type (input/output).
     */
    public static final int NUMERIC_ID_GROUP = 1;
    public static final int CUSTOM_ACTION_GROUP = 3;

    public static final String START_NODE_ID = "start";
    public static final String SEND_NODE_ID = "end";
    public static final String START_NODE_SOURCE_HANDLE_ID =
            START_NODE_ID + HANDLE_SEPARATOR + OUTPUT_KEYWORD;


    // --- NUMERIC HANDLE PATTERN ---
    public static final Pattern NUMERIC_HANDLE_PATTERN = Pattern.compile(
            String.format("^(\\d+)%s(%s|%s)$", q(FlowConstants.HANDLE_SEPARATOR),
                    q(FlowConstants.INPUT_KEYWORD), q(FlowConstants.OUTPUT_KEYWORD)),
            Pattern.CASE_INSENSITIVE);


    // --- CUSTOM NODE HANDLE PATTERN ---
    public static final Pattern CUSTOM_NODE_HANDLE_PATTERN = Pattern.compile(
            String.format("^(.*?)%s(\\d+)%s(%s|%s)%s%s$", q(FlowConstants.HANDLE_SEPARATOR),
                    q(FlowConstants.HANDLE_SEPARATOR), q(ApprovalResultStatus.ACCEPT.getValue()),
                    q(ApprovalResultStatus.REJECT.getValue()), q(FlowConstants.HANDLE_SEPARATOR),
                    q(FlowConstants.OUTPUT_KEYWORD)), Pattern.CASE_INSENSITIVE);


    // --- NODE ID HANDLE PATTERN ---
    public static final Pattern NODE_ID_HANDLE_PATTERN = Pattern.compile(
            String.format("^(.*?)%s(%s|%s)$", // Matches <anything_non_greedy>-<input|output>
                    q(FlowConstants.HANDLE_SEPARATOR), q(FlowConstants.INPUT_KEYWORD),
                    q(FlowConstants.OUTPUT_KEYWORD)), Pattern.CASE_INSENSITIVE);

    private static String q(String s) {
        return s;
    }


}
