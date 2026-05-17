package org.larik.three.domain.port.out;

import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;

public interface MissingTransactionRepository {

    void save(ComparisonTransactionResult c);
}
