package org.larik.three.infra.repository;

import lombok.extern.slf4j.Slf4j;
import org.larik.three.domain.model.ErrorPolicy;
import org.larik.three.domain.port.out.ErrorPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ErrorAdapter implements ErrorPort  {

    private final MongoTemplate mongoTemplate;

    public ErrorAdapter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void insert(ErrorPolicy e) {
        mongoTemplate.insert(e);
        log.warn("Successfully persisted error: {}", e.getMessage());
    }
}
