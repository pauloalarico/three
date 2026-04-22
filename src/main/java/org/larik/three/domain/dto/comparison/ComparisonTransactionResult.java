package org.larik.three.domain.dto.comparison;

import org.larik.three.domain.model.Transaction;

public record ComparisonTransactionResult(
        Transaction rawTransaction,
        Transaction expectedTransaction,
        ComparisonStatus status
) {

    public enum ComparisonStatus {
        MATCHED,
        DIVERGENT,
        MISSING,
        UNEXPECTED
    }
}


