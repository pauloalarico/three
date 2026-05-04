package org.larik.three.infra.batch.processor.report;

import org.jspecify.annotations.Nullable;
import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.larik.three.domain.model.AbstractReport;
import org.larik.three.domain.model.CompleteReport;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CompleteDataProcessor implements ItemProcessor<ComparisonTransactionResult, CompleteReport> {

    @Override
    public @Nullable CompleteReport process(ComparisonTransactionResult item) throws Exception {
        return new CompleteReport(buildAbstractReport(item), item.status()
        );
    }

    private static AbstractReport buildAbstractReport(ComparisonTransactionResult item) {
        return new AbstractReport(
                item.rawTransaction().getTransactionId(),
                item.rawTransaction().getClient().getClientId(),
                item.rawTransaction().getClient().getName(),
                item.expectedTransaction().getPayment().getDate(),
                item.rawTransaction().getPayment().getValue(),
                item.expectedTransaction().getPayment().getValue());
    }
}
