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


    public static final String START_NODE_ID = "start";
    public static final String START_NODE_SOURCE_HANDLE_ID =
            START_NODE_ID + HANDLE_SEPARATOR + OUTPUT_KEYWORD;


    // --- NUMERIC HANDLE PATTERN ---
    public static final Pattern CHANGE_STATUS_ID_HANDLE_PATTERN = Pattern.compile(
            String.format("^(\\d+)%s(%s|%s)$", FlowConstants.HANDLE_SEPARATOR,
                    FlowConstants.INPUT_KEYWORD, FlowConstants.OUTPUT_KEYWORD),
            Pattern.CASE_INSENSITIVE);
    public static final int CHANGE_STATUS_ID_HANDLE_PATTERN__CHANGE_STATUS_ID_INDEX = 1;


    // --- CUSTOM NODE HANDLE PATTERN ---
    public static final Pattern NODE_APPROVAL_HANDLE_PATTERN = Pattern.compile(
            String.format("^(.*?)%s(\\d+)%s(%s|%s)%s%s$", FlowConstants.HANDLE_SEPARATOR,
                    FlowConstants.HANDLE_SEPARATOR, ApprovalResultStatus.ACCEPT.getValue(),
                    ApprovalResultStatus.REJECT.getValue(), FlowConstants.HANDLE_SEPARATOR,
                    FlowConstants.OUTPUT_KEYWORD), Pattern.CASE_INSENSITIVE);
    public static final int NODE_APPROVAL_HANDLE_PATTERN__NODE_ID_INDEX = 1;
    public static final int NODE_APPROVAL_HANDLE_PATTERN__NODE_APPROVAL_ACTION_INDEX = 3;


    // --- NODE ID HANDLE PATTERN ---
    public static final Pattern NODE_ID_HANDLE_PATTERN = Pattern.compile(
            String.format("^(.*?)%s(%s|%s)$", FlowConstants.HANDLE_SEPARATOR,
                    FlowConstants.INPUT_KEYWORD, FlowConstants.OUTPUT_KEYWORD),
            Pattern.CASE_INSENSITIVE);
    public static final int NODE_ID_HANDLE_PATTERN__HANDLE_TYPE_INDEX = 2;

}
