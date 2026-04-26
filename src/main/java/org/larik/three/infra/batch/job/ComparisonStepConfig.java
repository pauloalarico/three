package org.larik.three.infra.batch.job;

import org.larik.three.domain.dto.comparison.ComparisonTransaction;
import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.larik.three.infra.batch.processor.comparison.TransactionComparison;
import org.larik.three.infra.batch.reader.ComparisonTransactionReader;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComparisonStepConfig {

    @Bean
    public Step comparison(JobRepository jobRepository, ComparisonTransactionReader reader,
                           TransactionComparison processor,
                           ItemWriter<ComparisonTransactionResult> writer) {
        return new StepBuilder("comparison-step", jobRepository)
                .<ComparisonTransaction, ComparisonTransactionResult>chunk(100)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
