package org.larik.three.infra.batch.job;

import org.larik.three.domain.model.Transaction;
import org.larik.three.infra.batch.processor.TransactionFileProcessor;
import org.larik.three.infra.batch.reader.TransactionAsyncReader;
import org.larik.three.infra.batch.reader.partitioner.TransactionPartitioner;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class TransactionJobConfig {

    @Bean
    public Job job (@Qualifier("step") Step step, JobRepository jobRepository, @Qualifier("comparison") Step comparison) {
        return new JobBuilder("sdsd", jobRepository)
                .start(step)
                .next(comparison)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, @Qualifier("workerStep")Step step, TaskExecutor taskExecutor) {
        return new StepBuilder("thread-partitioner", jobRepository)
                .partitioner("partition-reader", new TransactionPartitioner())
                .step(step)
                .taskExecutor(taskExecutor)
                .gridSize(3)
                .build();
    }

    @Bean
    public Step workerStep(JobRepository jobRepository,
                     TransactionAsyncReader reader,
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
