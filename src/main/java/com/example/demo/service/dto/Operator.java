package com.example.demo.service.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor
public enum Operator {

    BIGGER_THAN(">"),

    BIGGER_EQUAL_THAN(">="), LESS_EQUAL_THAN("<="), LESS_THAN("<"), AND("AND"), OR("OR"),
    TIME_END_OF_DATE("23:59:59"), TIME_START_OF_DATE("00:00:00"), LIKE_SYMBOL("%"), LIKE("LIKE"),
    EQUAL("="), NOT_EQUAL("!=");


    private String value;


    Operator(String string) {

    }
}
