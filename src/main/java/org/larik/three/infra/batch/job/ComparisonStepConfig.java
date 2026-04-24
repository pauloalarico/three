package org.larik.three.infra.batch.job;

import org.larik.three.domain.dto.comparison.ComparisonTransaction;
import org.larik.three.infra.batch.reader.ComparisonTransactionReader;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComparisonStepConfig {

    @Bean
    public Step comparison(JobRepository jobRepository, ComparisonTransactionReader reader) {
        return new StepBuilder("comparison-step", jobRepository)
                .<ComparisonTransaction, Object>chunk(100)
                .reader(reader)
                .processor()
                .writer()
                .build();
    }
}
