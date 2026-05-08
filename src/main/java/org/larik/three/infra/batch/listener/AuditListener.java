package org.larik.three.infra.batch.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.larik.three.domain.model.Audit;
import org.springframework.batch.core.listener.ChunkListener;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public class AuditListener implements ChunkListener<Object, Object> {

    private final MongoTemplate mongoTemplate;
    private final StepExecution stepExecution;

    private Instant timeStamp;

    @Override
    public void beforeChunk(Chunk<Object> chunk) {
       timeStamp = Instant.now();
       log.info("Starting process at chunk: {}", stepExecution.getStepName());
    }

    @Override
    public void afterChunk(Chunk<Object> chunk) {
        long duration = Duration.between(timeStamp, Instant.now()).toMillis();
        ExecutionContext ctx = stepExecution.getExecutionContext();
        ctx.putLong("audit.chunkProcessedInMs", duration);
        log.info("Chunk done in {} ms, wrote {} at mongo", duration, stepExecution.getWriteCount());
    }

    @Override
    public void onChunkError(Exception exception, Chunk<Object> chunk) {
        log.error("Chunk error at {}, caused by: {}", stepExecution.getStepName(), exception.getMessage());
    }
}
