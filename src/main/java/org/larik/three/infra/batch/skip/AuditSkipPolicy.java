package org.larik.three.infra.batch.skip;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.stereotype.Component;

@Component
public class AuditSkipPolicy implements SkipPolicy {

    private static final int MAX_SKIP = 10;

    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {

        if (skipCount > MAX_SKIP) {
            throw new SkipLimitExceededException(MAX_SKIP, t);
        }

        return true;
    }
}
