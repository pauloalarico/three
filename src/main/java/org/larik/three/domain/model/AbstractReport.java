package org.larik.three.domain.model;

import java.math.BigDecimal;

public record AbstractReport(
        String transactionId,
        Long clientId,
        String clientName,
        String expectedDate,
        BigDecimal rawValue,
        BigDecimal expectedValue
) {
}
