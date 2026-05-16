package org.larik.three.infra.batch.step;

import org.larik.three.domain.dto.comparison.ComparisonTransaction;
import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.larik.three.infra.batch.listener.AuditListener;
import org.larik.three.infra.batch.processor.comparison.TransactionComparison;
import org.larik.three.infra.batch.reader.impl.ComparisonTransactionReader;
import org.larik.three.infra.batch.skip.AuditSkipPolicy;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComparisonStepConfig {

    @Bean
    public Step comparison(JobRepository jobRepositoryMeta, ComparisonTransactionReader reader,
                           TransactionComparison processor,
                           ItemWriter<ComparisonTransactionResult> writer,
                           AuditListener auditListener, AuditSkipPolicy auditSkipPolicy) {
        return new StepBuilder("comparison-step", jobRepositoryMeta)
                .<ComparisonTransaction, ComparisonTransactionResult>chunk(100)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(auditListener)
                .skipPolicy(auditSkipPolicy)
                .build();
    }
}
