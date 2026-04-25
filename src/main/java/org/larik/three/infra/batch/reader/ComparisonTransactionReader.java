package org.larik.three.infra.batch.reader;

import org.jspecify.annotations.Nullable;
import org.larik.three.domain.dto.comparison.ComparisonTransaction;
import org.larik.three.domain.model.Transaction;
import org.larik.three.domain.utils.TransactionMapper;
import org.larik.three.domain.valueobject.TransactionTrustSource;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamException;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.stereotype.Component;

@Component
public class ComparisonTransactionReader implements ItemStreamReader<ComparisonTransaction> {

    private final ItemStreamReader<Transaction> rawTransactionReader;

    private final ItemStreamReader<TransactionTrustSource> expectedTransactionReader;

    private static final String RAW_PREFIX = "raw.";

    private static final String EXPECTED_PREFIX = "expected.";

    public ComparisonTransactionReader(ItemStreamReader<Transaction> rawTransactionReader, ItemStreamReader<TransactionTrustSource> expectedTransactionReader) {
        this.rawTransactionReader = rawTransactionReader;
        this.expectedTransactionReader = expectedTransactionReader;
    }

    @Override
    public @Nullable ComparisonTransaction read() throws Exception {
        var rawTransaction = rawTransactionReader.read();
        var expectedTransaction = expectedTransactionReader.read();

        if (rawTransaction == null && expectedTransaction == null) {
            return null;
        }

        var trustTransaction = expectedTransaction != null ? TransactionMapper.toTransaction(expectedTransaction)
                : null;

        return new ComparisonTransaction(rawTransaction, trustTransaction);
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        rawTransactionReader.open(prefix(executionContext, RAW_PREFIX));
        expectedTransactionReader.open(prefix(executionContext, EXPECTED_PREFIX));
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        rawTransactionReader.update(prefix(executionContext, RAW_PREFIX));
        expectedTransactionReader.update(prefix(executionContext, EXPECTED_PREFIX));

        mergeExecutionChild(executionContext, prefix(executionContext, RAW_PREFIX), RAW_PREFIX);
        mergeExecutionChild(executionContext, prefix(executionContext, EXPECTED_PREFIX), EXPECTED_PREFIX);
    }

    @Override
    public void close() throws ItemStreamException {
        rawTransactionReader.close();
        expectedTransactionReader.close();
    }

    private ExecutionContext prefix(ExecutionContext executionContext, String readerPrefix) {
        ExecutionContext ctxChild = new ExecutionContext();

        executionContext.entrySet().stream()
                .filter(p -> p.getKey().startsWith(readerPrefix))
                .forEach(p -> ctxChild.put(
                        p.getKey().substring(readerPrefix.length()), p.getValue()
                ));

        return ctxChild;
    }


    private void mergeExecutionChild(ExecutionContext executionContext, ExecutionContext ctxChild, String readerPrefix) {
         ctxChild.entrySet()
                .forEach(e -> executionContext.put(readerPrefix + e.getKey(), e.getValue()));
    }
}
