package org.larik.three.infra.repository;

import org.larik.three.domain.port.out.MongoAudit;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuditRepository implements MongoAudit  {

    public AuditRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private final MongoTemplate mongoTemplate;

    @Override
    public void insert(Object o) {
        mongoTemplate.insert(o);
    }
}
