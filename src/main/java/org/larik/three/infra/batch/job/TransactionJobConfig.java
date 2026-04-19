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
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.Future;

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
                     AsyncItemProcessor<Transaction, Transaction> processor,
                     AsyncItemWriter<Transaction> writer) {
        return new StepBuilder("teste", jobRepository)
                .<Transaction, Future<Transaction>>chunk(100)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public AsyncItemProcessor<Transaction, Transaction> asyncItemProcessor(TransactionFileProcessor processor, TaskExecutor taskExecutor) {
        AsyncItemProcessor<Transaction, Transaction> asyncProcessor = new AsyncItemProcessor<>(processor);
        asyncProcessor.setTaskExecutor(taskExecutor);
        return asyncProcessor;
    }

    @Bean
    public AsyncItemWriter<Transaction> asyncItemWriter(ItemWriter<Transaction> writer) {
        return new AsyncItemWriter<>(writer);
    }
}
