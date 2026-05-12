package org.larik.three.domain.dto.report;

import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.larik.three.domain.enums.PaymentStatus;
import org.larik.three.domain.model.Client;
import org.larik.three.domain.model.Payment;
import org.larik.three.domain.model.Transaction;

public record FinalReportSummary(
        Transaction rawTransaction,
        ExpectedTransaction expectedTransaction,
        ComparisonTransactionResult.ComparisonStatus status
) {

    public record ExpectedTransaction(
            Client client,
            Payment payment,
            PaymentStatus status
    ) {}
}
