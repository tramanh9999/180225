package com.example.demo.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class SqlDateRangeModel {

    SqlVariableAndValueModel endOfPreviousFromDate;
    SqlVariableAndValueModel endOfFromDate;
    SqlVariableAndValueModel startOfNextToDate;
    SqlVariableAndValueModel startOfToDate;

}