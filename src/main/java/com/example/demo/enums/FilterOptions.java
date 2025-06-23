package com.example.demo.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public enum FilterOptions {
    GREATER_THAN("greatThan"), GREATER_THAN_OR_EQUAL_TO("greateThanOrEqualTo"),
    LESS_THAN("lessThan"), LESS_THAN_OR_EQUAL_TO("lessThanOrEqualTo"), EQUALS("equals"),
    NOT_EQUALS("not equals"), BETWEEN("between"), BETWEEN_INCLUSIVE("betweenInclusive"),
    CONTAINS("contains"), ENDS_WITH("endsWith"),

    STARTS_WITH("startWith");;
    private String value;
}
