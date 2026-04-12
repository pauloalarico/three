package org.larik.three.infra.batch.job;

import org.larik.three.domain.model.Transaction;
import org.larik.three.infra.batch.processor.TransactionFileProcessor;
import org.larik.three.infra.batch.reader.TransactionReader;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionJobConfig {

    @Bean
    public Job job (Step step, JobRepository jobRepository) {
        return new JobBuilder("sdsd", jobRepository)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     TransactionReader reader,
                     TransactionFileProcessor processor,
                     ItemWriter<Transaction> writer) {
        return new StepBuilder("teste", jobRepository)
                .<Transaction, Transaction>chunk(100)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
