package org.larik.three.infra.batch.reader.impl;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.larik.three.domain.dto.comparison.ComparisonTransaction;
import org.larik.three.domain.model.Transaction;
import org.larik.three.domain.utils.TransactionMapper;
import org.larik.three.domain.valueobject.TransactionTrustSource;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamException;
import org.springframework.batch.infrastructure.item.ItemStreamReader;


@Slf4j
public class ComparisonTransactionReader implements ItemStreamReader<ComparisonTransaction> {

    private final ItemStreamReader<Transaction> rawTransactionReader;

    private final ItemStreamReader<TransactionTrustSource> expectedTransactionReader;

    private static final String RAW_PREFIX = "raw.";

    private static final String EXPECTED_PREFIX = "expected.";

    private Transaction rawBuffer;

    private Transaction expectedBuffer;

    public ComparisonTransactionReader(ItemStreamReader<Transaction> rawTransactionReader, ItemStreamReader<TransactionTrustSource> expectedTransactionReader) {
        this.rawTransactionReader = rawTransactionReader;
        this.expectedTransactionReader = expectedTransactionReader;
    }

    @Override
    public @Nullable ComparisonTransaction read() throws Exception {
        if (rawBuffer == null) {
            rawBuffer = rawTransactionReader.read();
        }

        if (expectedBuffer == null) {
            TransactionTrustSource buffer = expectedTransactionReader.read();
            expectedBuffer = buffer != null ? TransactionMapper.toTransaction(buffer) : null;
        }

        if (rawBuffer == null && expectedBuffer == null) {
            return null;
        }

        if (rawBuffer == null) {
            var comparison = new ComparisonTransaction(null, expectedBuffer);
            expectedBuffer = null;
            return comparison;

        }
        if (expectedBuffer == null) {
            var comparison = new ComparisonTransaction(rawBuffer, null);
            rawBuffer = null;
            return comparison;
        }

        log.info("raw client: {}, expected: {}", rawBuffer.getClient().getClientId(), expectedBuffer.getClient().getClientId() );

        Long rawClientId = rawBuffer.getClient().getClientId();
        Long expectedClientId = expectedBuffer.getClient().getClientId();
        int compareToExpected = rawClientId.compareTo(expectedClientId);

        log.info("comparision: {}", compareToExpected);

        if (compareToExpected == 0) {
            var comparison = new ComparisonTransaction(rawBuffer, expectedBuffer);
            rawBuffer = null;
            expectedBuffer = null;
            return comparison;
        } else if (compareToExpected < 0) {
            var comparison = new ComparisonTransaction(rawBuffer, null);
            rawBuffer = null;
            return comparison;
        } else {
            var comparison = new ComparisonTransaction(null, expectedBuffer);
            expectedBuffer = null;
            return comparison;
        }
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
