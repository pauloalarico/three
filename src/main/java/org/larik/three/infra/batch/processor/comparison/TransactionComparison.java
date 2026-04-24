package org.larik.three.infra.batch.processor.comparison;

import org.jspecify.annotations.Nullable;
import org.larik.three.domain.dto.comparison.ComparisonTransaction;
import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

public class TransactionComparison implements ItemProcessor<ComparisonTransaction, ComparisonTransactionResult> {

    @Value("${app.mongoCollection}")
    private String maxValueDiff;

    @Override
    public @Nullable ComparisonTransactionResult process(ComparisonTransaction item) throws Exception {
        var raw = item.rawTransaction();
        var expected = item.expectedTransaction();

        if (raw == null || expected == null) {
            return null;
        }

        if (!raw.getTransactionId().equals(expected.getTransactionId())) {
            return null;
        }

        int valueDifference = expected.getPayment().getValue().subtract(raw.getPayment().getValue()).intValue();
        if (valueDifference > Integer.parseInt(maxValueDiff)) {
            return null;
        }

        return new ComparisonTransactionResult(raw, expected,ComparisonTransactionResult.ComparisonStatus.MATCHED);
    }
}
