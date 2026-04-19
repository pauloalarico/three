package org.larik.three.infra.batch.reader.partitioner;

import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TransactionPartitionerConfig {

    @Bean
    public Step partitioner(JobRepository jobRepository, Step step, TaskExecutor taskExecutor) {
        return new StepBuilder("thread-partitioner", jobRepository)
                .partitioner("partition-reader", new MultiResourcePartitioner())
                .step(step)
                .taskExecutor(taskExecutor)
                .gridSize(4)
                .build();
    }


    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setQueueCapacity(3);
        executor.setMaxPoolSize(3);
        executor.setThreadNamePrefix("transaction-");
        return executor;
    }
}
