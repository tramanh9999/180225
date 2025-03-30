package com.example.demo.service.dto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


public class Abc {
    private static final String GREATER_THAN = "greatThan";

    private static final String GREATER_THAN_OR_EQUAL_TO = "greateThanOrEqualTo";

    private static final String LESS_THAN = "lessThan";
    private static final String LESS_THAN_OR_EQUAL_TO = "lessThanOrEqualTo";
    private static final String EQUALS = "equals";
    private static final String NOT_EQUALS = "not equals";
    private static final String BETWEEN = "between";
    private static final String BETWEEN_INCLUSIVE = "betweenInclusive";
    private static final String CONTAINS = "contains";
    private static final String ENDS_WITH = "endsWith";

    public static String convertDateValueToQuery(String date) {
        return "";
    }

    public static String convertFilterToSQL(String fieldName, ColumnFilterModel conditionObj,
                                            Class<?> fieldType,
                                            Map<String, Object> mapSqlVariableWithValue)
            throws BusinessException {
        String filterOption = conditionObj.getFilterOption();
        FilterValueModel fieldAndValues = conditionObj.getValue();

        FilterOptions[] optionValues = FilterOptions.values();
        String firstVarForPrepareSqlMap = fieldName + "1";
        String firstVarSql = ":" + firstVarForPrepareSqlMap;
        String startDateStr = fieldAndValues.getFromValue().trim().toLowerCase();
        Optional<FilterOptions> allowFilterOptions = Arrays.stream(optionValues)
                .filter(item -> Objects.equals(item.getValue(), filterOption)).findFirst();
        if (allowFilterOptions.isEmpty()) {
            throw new BusinessException(ErrorCode.TABLE_FILTER_INVALID_OPTION, filterOption);
        }
        if (Date.class == fieldType) {
            Instant startDateInstant;
            try {
                startDateInstant = Instant.parse(startDateStr);
            } catch (DateTimeParseException exp) {
                throw new IllegalArgumentException("Invalid datetime: " + startDateStr);
            }
            ZonedDateTime utcZonedDateTime = startDateInstant.atZone(ZoneId.systemDefault());
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(DateUtils.FORMAT_YYYY_MM_DD_Z);
            startDateStr = utcZonedDateTime.format(formatter).substring(0, 10);
            String startOfFromDateSqlVarForMap = firstVarForPrepareSqlMap + "Start";
            String startOfFromDateSqlVar = ":" + startOfFromDateSqlVarForMap;
            String startOfFromDateQueryVariable = convertDateValueToQuery(startOfFromDateSqlVar);
            String startOfFromDateVal = startDateStr + Operator.TIME_START_OF_DATE.getValue();
            String endOfFromDateSqlVarForMap = firstVarForPrepareSqlMap + "End";
            String endOfFromDateSqlVar = ":" + endOfFromDateSqlVarForMap;
            String endOfFromDateQueryVariable = convertDateValueToQuery(endOfFromDateSqlVar);
            String endOfFromDateVal = startDateStr + Operator.TIME_END_OF_DATE.getValue();
            Instant endDateInstant;
            String endDateStr = fieldAndValues.getToValue().trim().toLowerCase();
            try {
                endDateInstant = Instant.parse(endDateStr);
                endDateStr = endDateInstant.atZone(ZoneId.systemDefault()).format(formatter)
                        .substring(0, 10);
            } catch (DateTimeParseException exp) {
                throw new IllegalArgumentException("Invalid datetime: " + endDateStr);
            }
            String startOfEndDateSqlVarForMap = firstVarForPrepareSqlMap + "Start";
            String startOfEndDateSqlVar = ":" + startOfEndDateSqlVarForMap;
            String startOfEndDateQueryVariable = convertDateValueToQuery(startOfEndDateSqlVar);
            String startOfEndDateVal = endDateStr + Operator.TIME_START_OF_DATE.getValue();
            String endOfEndDateSqlVarForMap = firstVarForPrepareSqlMap + "End";
            String endOfEndDateSqlVar = ":" + endOfEndDateSqlVarForMap;
            String endOfEndDateQueryVariable = convertDateValueToQuery(endOfEndDateSqlVar);
            String endOfEndDateVal = endDateStr + Operator.TIME_END_OF_DATE.getValue();


            return " " + switch (allowFilterOptions.get()) {
                case GREATER_THAN -> {
                    mapSqlVariableWithValue.put(endOfFromDateSqlVarForMap, endOfFromDateVal);
                    yield fieldName + Operator.BIGGER_THAN.getValue() + endOfFromDateQueryVariable;
                }
                case GREATER_THAN_OR_EQUAL_TO -> {
                    mapSqlVariableWithValue.put(startOfFromDateSqlVarForMap, startOfFromDateVal);
                    yield fieldName + Operator.BIGGER_EQUAL_THAN.getValue() +
                            startOfFromDateQueryVariable;
                }
                case LESS_THAN -> {
                    mapSqlVariableWithValue.put(startOfFromDateSqlVarForMap, startOfFromDateVal);
                    yield fieldName + Operator.LESS_THAN.getValue() + startOfFromDateQueryVariable;
                }
                case LESS_THAN_OR_EQUAL_TO -> {
                    mapSqlVariableWithValue.put(startOfFromDateSqlVarForMap, startOfFromDateVal);
                    mapSqlVariableWithValue.put(endOfFromDateSqlVarForMap, endOfFromDateVal);
                    yield fieldName + Operator.LESS_EQUAL_THAN.getValue() +
                            endOfFromDateQueryVariable;
                }
                case EQUALS -> {
                    mapSqlVariableWithValue.put(startOfFromDateSqlVarForMap, startOfFromDateVal);
                    mapSqlVariableWithValue.put(endOfFromDateSqlVarForMap, endOfFromDateVal);
                    yield fieldName + Operator.BIGGER_EQUAL_THAN.getValue() +
                            startOfFromDateQueryVariable + Operator.AND.getValue() + fieldName +
                            Operator.LESS_EQUAL_THAN.getValue() + endOfFromDateQueryVariable;
                }
                case NOT_EQUALS -> {
                    mapSqlVariableWithValue.put(startOfFromDateSqlVarForMap, startOfFromDateVal);
                    yield "(" + fieldName + Operator.LESS_THAN.getValue() +
                            startOfFromDateQueryVariable + Operator.OR.getValue() + fieldName +
                            Operator.BIGGER_THAN.getValue() + startOfFromDateQueryVariable + ")";
                }
                case BETWEEN_INCLUSIVE -> {
                    mapSqlVariableWithValue.put(startOfFromDateSqlVarForMap, startOfFromDateVal);
                    mapSqlVariableWithValue.put(endOfEndDateSqlVarForMap, endOfEndDateVal);
                    yield "(" + fieldName + Operator.LESS_EQUAL_THAN.getValue() +
                            endOfEndDateQueryVariable + Operator.AND.getValue() + fieldName +
                            Operator.BIGGER_EQUAL_THAN.getValue() + startOfFromDateQueryVariable +
                            ")";
                }
                case BETWEEN -> {
                    mapSqlVariableWithValue.put(startOfFromDateSqlVarForMap, startOfFromDateVal);
                    mapSqlVariableWithValue.put(endOfFromDateSqlVarForMap, endOfFromDateVal);
                    yield "(" + fieldName + Operator.LESS_THAN.getValue() +
                            startOfEndDateQueryVariable + Operator.AND.getValue() + fieldName +
                            Operator.BIGGER_THAN.getValue() + endOfFromDateQueryVariable + ")";
                }
                default ->
                        throw new IllegalArgumentException("Unknown filter mode: " + filterOption);
            };
        }
        if (String.class == fieldType) {
            return " " + switch (allowFilterOptions.get()) {
                case CONTAINS -> {
                    mapSqlVariableWithValue.put(firstVarForPrepareSqlMap,
                            Operator.LIKE_SYMBOL.getValue() + startDateStr +
                                    Operator.LIKE_SYMBOL.getValue());
                    yield "LOWER(" + fieldName + ")" + Operator.LIKE.getValue() + firstVarSql;
                }
                case STARTS_WITH -> {
                    mapSqlVariableWithValue.put(firstVarForPrepareSqlMap,
                            startDateStr + Operator.LIKE_SYMBOL.getValue());
                    yield "LOWER(" + fieldName + ")" + Operator.LIKE.getValue() + firstVarSql;
                }
                case ENDS_WITH -> {
                    mapSqlVariableWithValue.put(firstVarForPrepareSqlMap,
                            Operator.LIKE_SYMBOL.getValue() + startDateStr);
                    yield "LOWER(" + fieldName + ")" + Operator.LIKE.getValue() + firstVarSql;
                }
                case EQUALS -> {
                    mapSqlVariableWithValue.put(firstVarForPrepareSqlMap, startDateStr);
                    yield "LOWER(" + fieldName + ")" + Operator.EQUAL.getValue() + firstVarSql;
                }
                case NOT_EQUALS -> {
                    mapSqlVariableWithValue.put(firstVarForPrepareSqlMap, startDateStr);
                    yield "LOWER(" + fieldName + ")" + Operator.NOT_EQUAL.getValue() + firstVarSql;
                }      // bellow for number
                default ->
                        throw new IllegalArgumentException("Unknown filter mode: " + filterOption);
            };
        }

        return " " + switch (allowFilterOptions.get()) {
            case GREATER_THAN -> fieldName + Operator.BIGGER_THAN.getValue() + firstVarSql;
            case GREATER_THAN_OR_EQUAL_TO ->
                    fieldName + Operator.BIGGER_EQUAL_THAN.getValue() + firstVarSql;
            case LESS_THAN -> fieldName + Operator.LESS_THAN.getValue() + firstVarSql;
            case LESS_THAN_OR_EQUAL_TO ->
                    fieldName + Operator.LESS_EQUAL_THAN.getValue() + firstVarSql;
            case EQUALS -> fieldName + Operator.EQUAL.getValue() + firstVarSql;
            case NOT_EQUALS ->
                    "LOWER(" + fieldName + ")" + Operator.NOT_EQUAL.getValue() + firstVarSql;
            default -> throw new IllegalArgumentException("Unknown filter mode: " + filterOption);
        };
    }
}





