package org.larik.three.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import java.math.BigDecimal;

@JsonIgnoreType
public sealed interface Report permits CompleteReport, DivergentDataReport {
    AbstractReport report();

    default String transactionId() {return report().transactionId();}
    default Long clientId() {return report().clientId();}
    default String clientName() {return report().clientName();}
    default String expectedDate() {return report().expectedDate();}
    default BigDecimal rawValue() {return  report().rawValue();}
    default BigDecimal expectedValue() {return report().expectedValue();}
}
