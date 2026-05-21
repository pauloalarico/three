package org.larik.three.domain.port.out;

import org.larik.three.domain.model.Audit;

public interface MongoAudit {

    void insert(Audit a);
}
