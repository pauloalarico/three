package org.larik.three.infra.batch.step;

import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.larik.three.domain.model.ProcessedTransaction;
import org.larik.three.infra.batch.listener.AuditListener;
import org.larik.three.infra.batch.processor.financial.FinancialPositionCalculator;
import org.larik.three.infra.batch.skip.AuditSkipPolicy;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinancialStepConfig {

    @Bean
    public Step financialStep(JobRepository jobRepository, ItemReader<ComparisonTransactionResult> readerComparison,
                              FinancialPositionCalculator processor, ItemWriter<ProcessedTransaction> writer,
                              AuditListener auditListener, AuditSkipPolicy auditSkipPolicy) {
        return new StepBuilder("financialStep", jobRepository)
                .<ComparisonTransactionResult, ProcessedTransaction>chunk(15)
                .reader(readerComparison)
                .processor(processor)
                .writer(writer)
                .listener(auditListener)
                .skipPolicy(auditSkipPolicy)
                .build();
    }

}
