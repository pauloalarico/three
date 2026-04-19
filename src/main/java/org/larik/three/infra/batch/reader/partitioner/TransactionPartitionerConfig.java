package org.larik.three.infra.batch.reader.partitioner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TransactionPartitionerConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setQueueCapacity(3);
        executor.setMaxPoolSize(3);
        executor.setThreadNamePrefix("transaction-");
        executor.initialize();
        return executor;
    }
}
