package org.larik.three.infra.batch;

import lombok.RequiredArgsConstructor;
import org.larik.three.domain.model.Transaction;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.data.MongoItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@RequiredArgsConstructor
public class TransactionWriterConfig {

    private String collection;

    private final MongoTemplate mongoTemplate;

    @Bean
    public ItemWriter<Transaction> writer() {
        MongoItemWriter<Transaction> writer = new MongoItemWriter<>(mongoTemplate);
        writer.setCollection("writer-teste");
        return writer;
    }


}
