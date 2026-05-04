package org.larik.three.infra.batch.processor.report;

import org.jspecify.annotations.Nullable;
import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.larik.three.domain.model.DivergentDataReport;
import org.larik.three.domain.service.AbstractBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DivergentDataProcessor implements ItemProcessor<ComparisonTransactionResult, DivergentDataReport> {

    @Override
    public @Nullable DivergentDataReport process(ComparisonTransactionResult item) throws Exception {
        return new DivergentDataReport(AbstractBuilder.build(item), calculateDelta(item));
    }

    private static BigDecimal calculateDelta(ComparisonTransactionResult item) {
        return item.expectedTransaction().getPayment().getValue().subtract(item.expectedTransaction().getPayment().getValue());
    }
}
