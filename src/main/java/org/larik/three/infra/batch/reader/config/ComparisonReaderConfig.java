package org.larik.three.infra.batch.reader.config;

import lombok.RequiredArgsConstructor;
import org.larik.three.domain.model.Transaction;
import org.larik.three.domain.valueobject.TransactionTrustSource;
import org.larik.three.infra.batch.reader.impl.ComparisonTransactionReader;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.data.builder.MongoCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class ComparisonReaderConfig {

    private final MongoTemplate mongoTemplate;

    @Bean
    public ComparisonTransactionReader comparisonTransactionReader(ItemStreamReader<Transaction> rawTransactionReader,
                                                                   ItemStreamReader<TransactionTrustSource> expectedTransactionsReader) {
        return new ComparisonTransactionReader(rawTransactionReader, expectedTransactionsReader);
    }

    /*
    * Here is the trick for the paralleling reading. The Cursor reads by order and not saving the state of the threads
    * TODO implement a execution ctx save but saving the sort to not loosing the sort.
     */
    @Bean
    public ItemStreamReader<Transaction> rawTransactionReader() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, "client.clientId"));

        return new MongoCursorItemReaderBuilder<Transaction>()
                .name("raw-tramsaction-reader")
                .collection("writer-teste")
                .template(mongoTemplate)
                .query(query)
                .targetType(Transaction.class)
                .saveState(false)
                .sorts(Collections.singletonMap("client.clientId", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemStreamReader<TransactionTrustSource> expectedTransactionsReader() {
        return new MongoCursorItemReaderBuilder<TransactionTrustSource>()
                .name("raw-tramsaction-reader")
                .collection("trust-source")
                .template(mongoTemplate)
                .query(new Query())
                .targetType(TransactionTrustSource.class)
                .sorts(Collections.singletonMap("clientId", Sort.Direction.ASC))
                .build();
    }

}
