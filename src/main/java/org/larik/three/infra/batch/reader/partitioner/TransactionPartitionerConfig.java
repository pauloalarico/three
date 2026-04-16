package org.larik.three.infra.batch.reader.partitioner;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class TransactionPartitionerConfig {

//    @Bean
//    public Step partioner(JobRepository jobRepository) {
//        return new StepBuilder(jobRepository, "thread-partitioner")
//                .
//    }


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
