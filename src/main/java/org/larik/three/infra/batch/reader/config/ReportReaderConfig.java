package org.larik.three.infra.batch.reader.config;

import lombok.RequiredArgsConstructor;
import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.larik.three.domain.model.ProcessedTransaction;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.data.builder.MongoCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class ReportReaderConfig {

    private final MongoTemplate mongoTemplate;

    @Bean
    public ItemReader<ComparisonTransactionResult> completeTransactionsReader() {
        return new MongoCursorItemReaderBuilder<ComparisonTransactionResult>()
                .name("completeReport")
                .template(mongoTemplate)
                .collection("reconciliation_result")
                .query(new Query())
                .targetType(ComparisonTransactionResult.class)
                .sorts(Collections.singletonMap("client.clientId", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemReader<ComparisonTransactionResult> divergentDataReader() {
        return new MongoCursorItemReaderBuilder<ComparisonTransactionResult>()
                .name("divergentDataReport")
                .template(mongoTemplate)
                .collection("reconciliation_result")
                .jsonQuery("{ \"status\": \"DIVERGENT\"}")
                .sorts(Collections.singletonMap("client.clientId", Sort.Direction.ASC))
                .targetType(ComparisonTransactionResult.class)
                .build();
    }



    @Bean
    public ItemReader<ProcessedTransaction> financialProjectionReader() {
        return new MongoCursorItemReaderBuilder<ProcessedTransaction>()
                .name("financialProjectionReport")
                .template(mongoTemplate)
                .collection("financial_position")
                .query(new Query())
                .targetType(ProcessedTransaction.class)
                .sorts(Collections.singletonMap("client.clientId", Sort.Direction.ASC))
                .build();
    }

//    @Bean
//    public ItemReader<ProcessedTransaction> divergentDataReader() {
//        return new MongoCursorItemReaderBuilder<ProcessedTransaction>()
//                .name("divergentDataReader")
//                .template(mongoTemplate)
//                .collection("financial_position")
//                .jsonQuery("{ \"status\": \"DIVERGENT\"}")
//                .sorts(Collections.singletonMap("client.clientId", Sort.Direction.ASC))
//                .targetType(ProcessedTransaction.class)
//                .build();
//    }
}
