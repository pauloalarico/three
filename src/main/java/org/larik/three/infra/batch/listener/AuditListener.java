package org.larik.three.infra.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.listener.ChunkListener;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
public class AuditListener implements ChunkListener<Object, Object> {

    private Instant timeStamp;

    private StepExecution stepExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }


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
