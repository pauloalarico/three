package org.larik.three.infra.batch.processor.comparison;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.larik.three.domain.dto.comparison.ComparisonTransaction;
import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.larik.three.domain.port.out.MissingTransactionRepository;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TransactionComparison implements ItemProcessor<ComparisonTransaction, ComparisonTransactionResult> {

    private final MissingTransactionRepository redisService;

    @Value("${app.maxValueDifference}")
    private String maxValueDiff;

    @Override
    public @Nullable ComparisonTransactionResult process(ComparisonTransaction item) throws Exception {
        var raw = item.rawTransaction();
        var expected = item.expectedTransaction();

        if (raw == null && expected == null) {
            var comparison = new ComparisonTransactionResult(null, null, ComparisonTransactionResult.ComparisonStatus.MISSING);
            redisService.save(comparison);
            return comparison;
        }

        if (raw != null && expected == null) {
            return new ComparisonTransactionResult(raw, null, ComparisonTransactionResult.ComparisonStatus.UNEXPECTED);
        }

        if(raw == null) {
            var comparison = new ComparisonTransactionResult(null, expected, ComparisonTransactionResult.ComparisonStatus.MISSING);
            redisService.save(comparison);
            return comparison;
        }

        if (raw.equals(expected)) {
            var valueDifference = expected.getPayment().getValue().subtract(raw.getPayment().getValue());
            int isDiffMoreThanExpected = valueDifference.compareTo(new BigDecimal(maxValueDiff));
            return isDiffMoreThanExpected == 0 ? new ComparisonTransactionResult(raw, expected, ComparisonTransactionResult.ComparisonStatus.DIVERGENT)
                    : new ComparisonTransactionResult(raw, expected, ComparisonTransactionResult.ComparisonStatus.MATCHED);
        }

        return new ComparisonTransactionResult(raw, expected, ComparisonTransactionResult.ComparisonStatus.DIVERGENT);
    }

}
