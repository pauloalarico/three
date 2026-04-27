package org.larik.three.infra.batch.reader.config;

import lombok.RequiredArgsConstructor;
import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.data.builder.MongoCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class FinancialPositionConfig {

    private final MongoTemplate mongoTemplate;

    @Value("${app.comparisonCollection}")
    private String reconciliationCollection;

    @Bean
    public ItemStreamReader<ComparisonTransactionResult> readerComparison() {
        return new MongoCursorItemReaderBuilder<ComparisonTransactionResult>()
                .name("financialPositionReader")
                .template(mongoTemplate)
                .collection(reconciliationCollection)
                .jsonQuery("{ status: { $in: [\"MATCHED\", \"DIVERGENT\"]}}")
                .sorts(Collections.singletonMap("rawTransaction.clientId", Sort.Direction.ASC))
                .targetType(ComparisonTransactionResult.class)
//                .saveState(false)
                .build();
    }

}
