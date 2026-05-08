package org.larik.three.domain.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("batch_audit ")
public record Audit(
        String stepName,
        LocalDateTime chunkStartedAt,
        String finishedAt,
        Long durationExecutionInMs,
        Long documentCount,
        LocalDateTime createdAt
) {

    public Audit(String stepName, LocalDateTime chunkStartedAt, String finishedAt, Long durationExecutionInMs, Long documentCount) {
        this(stepName, chunkStartedAt, finishedAt, durationExecutionInMs, documentCount, LocalDateTime.now());
    }
}
