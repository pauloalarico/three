package org.larik.three.domain.dto.comparison;

import org.larik.three.domain.model.Transaction;

public record ComparisonTransaction(
        Transaction rawTransaction,
        Transaction expectedTransaction
) {
}
