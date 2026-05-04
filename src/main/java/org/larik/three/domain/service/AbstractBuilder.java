package org.larik.three.domain.service;

import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.larik.three.domain.model.AbstractReport;

public final class AbstractBuilder {

    public static AbstractReport build(ComparisonTransactionResult item) {
        return new AbstractReport(
                item.rawTransaction().getTransactionId(),
                item.rawTransaction().getClient().getClientId(),
                item.rawTransaction().getClient().getName(),
                item.expectedTransaction().getPayment().getDate(),
                item.rawTransaction().getPayment().getValue(),
                item.expectedTransaction().getPayment().getValue());
    }
}

