package org.larik.three.infra.batch.processor.transaction;

import org.jspecify.annotations.Nullable;
import org.larik.three.domain.model.Transaction;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionFileProcessor implements ItemProcessor<Transaction, Transaction> {

    @Override
    public @Nullable Transaction process(Transaction item) throws Exception {
        item.setTransactionId(UUID.randomUUID().toString());
        return item;
    }
}