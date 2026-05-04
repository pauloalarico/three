package org.larik.three.domain.model;

import java.math.BigDecimal;

public record FinancialPositionReport(
        String referenceDate,
        BigDecimal expectedValue,
        BigDecimal receivedValue,
        BigDecimal differenceValue,
        BigDecimal firstMonthProjection,
        BigDecimal secondMonthProjection,
        BigDecimal thirdMonthProjection
) {
}
