package org.larik.three.infra.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.larik.three.domain.model.Transaction;
import org.larik.three.infra.batch.listener.AuditListener;
import org.larik.three.infra.batch.processor.transaction.TransactionFileProcessor;
import org.larik.three.infra.batch.reader.impl.TransactionAsyncReader;
import org.larik.three.infra.batch.reader.partitioner.TransactionPartitioner;
import org.larik.three.infra.batch.skip.AuditSkipPolicy;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
@Configuration
public class TransactionJobConfig {

    @Bean
    public Job job(@Qualifier("step") Step step,
                   JobRepository jobRepositoryMeta,
                   @Qualifier("comparison") Step comparison,
                   Step financialStep,
                   Step parallelReportStep) {
        return new JobBuilder("financial-job'", jobRepositoryMeta)
                .start(step)
                .next(comparison)
                .next(financialStep)
                .next(parallelReportStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepositoryMeta,
                     @Qualifier("workerStep") Step step,
                     TaskExecutor taskExecutor) {
        return new StepBuilder("thread-partitioner", jobRepositoryMeta)
                .partitioner("partition-reader", new TransactionPartitioner())
                .step(step)
                .taskExecutor(taskExecutor)
                .gridSize(3)
                .build();
    }

    @Bean
    public Step workerStep(JobRepository jobRepositoryMeta,
                           TransactionAsyncReader reader,
                           TransactionFileProcessor processor,
                           ItemWriter<Transaction> writer,
                           AuditListener auditListener, AuditSkipPolicy auditSkipPolicy) {
        return new StepBuilder("worker-thread-step", jobRepositoryMeta)
                .<Transaction, Transaction>chunk(100)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(auditListener)
                .skipPolicy(auditSkipPolicy)
                .build();
    }

    @Bean
    @StepScope
    public AuditListener auditListener(MongoTemplate mongoTemplate,
                                       @Value("#{stepExecution}") StepExecution stepExecution) {
       return new AuditListener(mongoTemplate, stepExecution);
    }

}
