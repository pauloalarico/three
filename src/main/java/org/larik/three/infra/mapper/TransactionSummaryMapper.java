package org.larik.three.infra.mapper;

import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.larik.three.domain.model.TransactionSummary;
import org.springframework.stereotype.Component;

@Component
public class TransactionSummaryMapper {

    public TransactionSummary toSummary(ComparisonTransactionResult item) {
        var raw = item.rawTransaction();
        var expected = item.expectedTransaction();

        return new TransactionSummary(
                raw.getTransactionId(),
                raw.getClient().getClientId(),
                raw.getClient().getName(),
                expected.getPayment().getDate(),
                raw.getPayment().getValue(),
                expected.getPayment().getValue());
    }
}

