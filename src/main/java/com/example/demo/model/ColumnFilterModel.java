package com.example.demo.model;

public class ColumnFilterModel {


    private String filterOption;
    private FilterValueModel value;

    public String getFilterOption() {
        return filterOption;
    }

    public void setFilterOption(String filterOption) {
        this.filterOption = filterOption;
    }

    public FilterValueModel getValue() {
        return value;
    }

    public void setValue(FilterValueModel value) {
        this.value = value;
    }
}
