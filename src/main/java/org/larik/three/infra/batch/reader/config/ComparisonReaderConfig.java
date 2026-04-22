package org.larik.three.infra.batch.reader.config;

import lombok.RequiredArgsConstructor;
import org.larik.three.domain.model.Transaction;
import org.larik.three.infra.batch.reader.ComparisonTransactionReader;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.data.builder.MongoCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class ComparisonReaderConfig {

    private final MongoTemplate mongoTemplate;

    @Bean
    public ComparisonTransactionReader comparisonTransactionReader(ItemStreamReader<Transaction> rawTransactionReader,
                                                                   ItemStreamReader<Transaction> expectedTransactionsReader) {
        return new ComparisonTransactionReader(rawTransactionReader, expectedTransactionsReader);
    }

    @Bean
    public ItemStreamReader<Transaction> rawTransactionReader() {
        return new MongoCursorItemReaderBuilder<Transaction>()
                .name("raw-tramsaction-reader")
                .collection("writer-test")
                .template(mongoTemplate)
                .targetType(Transaction.class)
                .sorts(Collections.singletonMap("clientId", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemStreamReader<Transaction> expectedTransactionsReader() {
        return new MongoCursorItemReaderBuilder<Transaction>()
                .name("raw-tramsaction-reader")
                .collection("trust-source")
                .template(mongoTemplate)
                .targetType(Transaction.class)
                .sorts(Collections.singletonMap("client.clientId", Sort.Direction.ASC))
                .build();
    }

}
