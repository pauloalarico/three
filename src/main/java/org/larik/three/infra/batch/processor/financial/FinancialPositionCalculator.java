package org.larik.three.infra.batch.processor.financial;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.larik.three.domain.model.*;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class FinancialPositionCalculator implements ItemProcessor<ComparisonTransactionResult, ProcessedTransaction> {

    private final BigDecimal selicTaxCalculator;

    private static final int[] MONTHS = {1, 2, 3};

    @Override
    public @Nullable ProcessedTransaction process(ComparisonTransactionResult item) throws Exception {
        BigDecimal diff = item.expectedTransaction().getPayment().getValue().subtract(item.rawTransaction().getPayment().getValue());
        Transaction raw = item.rawTransaction();
        Transaction expected = item.expectedTransaction();

        return ProcessedTransaction.builder().
                client(buildClient(raw))
                .status((item.status()))
                .remittance(buildRemittance(raw, expected))
                .balance(buildBalance(raw, expected, diff))
                .build();
    }

    private Client buildClient(Transaction raw) {
        return Client.builder()
                .clientId(raw.getClient().getClientId())
                .name(raw.getClient().getName())
                .build();
    }

    private Remittance buildRemittance(Transaction raw, Transaction expected) {
        return new Remittance(
                new RawRemittance(raw.getPayment().getDate(), raw.getPayment().getValue()),
                new ExpectedRemittance(expected.getPayment().getDate(), expected.getPayment().getValue())
        );
    }

    private Balance buildBalance(Transaction raw, Transaction expected, BigDecimal diff) {
        return Balance.builder()
                .realBalance(raw.getPayment().getValue())
                .expectedAmount(expected.getPayment().getValue())
                .balanceDifference(diff)
                .taxBalanceWithMonth(taxBalanceCalculator(raw.getPayment().getValue(), MONTHS[0]))
                .taxBalanceWithTwoMonths((taxBalanceCalculator(raw.getPayment().getValue(), MONTHS[1])))
                .taxBalanceWithThreeMonths((taxBalanceCalculator(raw.getPayment().getValue(), MONTHS[2])))
                .build();
    }

    private BigDecimal taxBalanceCalculator(BigDecimal value, int month) {
        var tax = value.multiply(selicTaxCalculator).multiply(BigDecimal.valueOf(month));
        return value.add(tax).setScale(2, RoundingMode.HALF_UP);
    }

}
