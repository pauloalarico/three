package org.larik.three.infra.batch.processor.report;

import org.jspecify.annotations.Nullable;
import org.larik.three.domain.model.FinancialPositionReport;
import org.larik.three.domain.model.ProcessedTransaction;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FinancialReportProcessor implements ItemProcessor<ProcessedTransaction, FinancialPositionReport> {

    @Override
    public @Nullable FinancialPositionReport process(ProcessedTransaction item) throws Exception {
        return new FinancialPositionReport(
                item.getRemittance().raw().date(),
                item.getBalance().getExpectedAmount(),
                item.getBalance().getRealBalance(),
                item.getBalance().getBalanceDifference(),
                item.getBalance().getTaxBalanceWithMonth(),
                item.getBalance().getTaxBalanceWithTwoMonths(),
                item.getBalance().getTaxBalanceWithThreeMonths()
        );
    }
}
