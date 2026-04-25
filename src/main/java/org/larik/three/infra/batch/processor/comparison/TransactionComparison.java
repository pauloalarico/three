package org.larik.three.infra.batch.processor.comparison;

import org.jspecify.annotations.Nullable;
import org.larik.three.domain.dto.comparison.ComparisonTransaction;
import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TransactionComparison implements ItemProcessor<ComparisonTransaction, ComparisonTransactionResult> {

    @Value("${app.mongoCollection}")
    private String maxValueDiff;

    @Override
    public @Nullable ComparisonTransactionResult process(ComparisonTransaction item) throws Exception {
        var raw = item.rawTransaction();
        var expected = item.expectedTransaction();

        if (raw == null && expected == null) {
            return new ComparisonTransactionResult(null, null, ComparisonTransactionResult.ComparisonStatus.MISSING);
        }

        if (raw != null && expected == null) {
            return new ComparisonTransactionResult(raw, null, ComparisonTransactionResult.ComparisonStatus.UNEXPECTED);
        }

        if(raw == null) {
            return new ComparisonTransactionResult(null, expected, ComparisonTransactionResult.ComparisonStatus.MISSING);
        }

        if (raw.equals(expected)) {
            int valueDifference = expected.getPayment().getValue().subtract(raw.getPayment().getValue()).intValue();
            boolean isDiffMoreThanExpected = valueDifference > Integer.parseInt(maxValueDiff);
            return isDiffMoreThanExpected ? new ComparisonTransactionResult(raw, expected, ComparisonTransactionResult.ComparisonStatus.DIVERGENT)
                    : new ComparisonTransactionResult(raw, expected, ComparisonTransactionResult.ComparisonStatus.MATCHED);
        }

        return new ComparisonTransactionResult(raw, expected, ComparisonTransactionResult.ComparisonStatus.UNEXPECTED);
    }

}
