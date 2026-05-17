package org.larik.three.domain.model;

import java.math.BigDecimal;

public record TransactionSummary(
        String transactionId,
        Long clientId,
        String clientName,
        String expectedDate,
        BigDecimal rawValue,
        BigDecimal expectedValue
) {
}
