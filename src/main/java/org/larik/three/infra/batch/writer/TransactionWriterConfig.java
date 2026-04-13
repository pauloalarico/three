package org.larik.three.infra.batch.writer;

import lombok.RequiredArgsConstructor;
import org.larik.three.domain.model.Transaction;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@RequiredArgsConstructor
public class TransactionWriterConfig {

    @Value("${app.mongoCollection}")
    private String collection;

    private final MongoTemplate mongoTemplate;

    @Bean
    public ItemWriter<Transaction> writer() {
        MongoItemWriter<Transaction> writer = new MongoItemWriter<>(mongoTemplate);
        writer.setCollection(collection);
        return writer;
    }


}
