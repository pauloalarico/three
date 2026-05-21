package org.larik.three.domain.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "skip_audit")
public record ErrorPolicy (
        long commitCount,
        String cause,
        String step,
        long countSkip,
        String createdAt
){
}
