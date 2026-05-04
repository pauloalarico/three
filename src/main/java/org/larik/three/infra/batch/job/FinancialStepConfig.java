package org.larik.three.infra.batch.job;

import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.larik.three.domain.model.ProcessedTransaction;
import org.larik.three.infra.batch.processor.financial.FinancialPositionCalculator;
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
                              FinancialPositionCalculator processor, ItemWriter<ProcessedTransaction> writer) {
        return new StepBuilder("financialStep", jobRepository)
                .<ComparisonTransactionResult, ProcessedTransaction>chunk(15)
                .reader(readerComparison)
                .processor(processor)
                .writer(writer)
                .build();
    }

}
