package org.larik.three.domain.valueobject;

import java.math.BigDecimal;

public record TransactionTrustSource (
        String transactionId,
        Long clientId,
        String clientName,
        BigDecimal amount,
        String date,
        String status
) {
}
