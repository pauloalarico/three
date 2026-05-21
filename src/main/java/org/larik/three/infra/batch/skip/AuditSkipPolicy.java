package org.larik.three.infra.batch.skip;

import org.larik.three.domain.model.ErrorPolicy;
import org.larik.three.domain.port.out.ErrorPort;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AuditSkipPolicy implements SkipPolicy {

    private static final int MAX_SKIP = 10;

    private final ErrorPort errorPort;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    public AuditSkipPolicy(ErrorPort errorPort) {
        this.errorPort = errorPort;
    }

    @Override
    public boolean shouldSkip(Throwable t, long skipCount) {

        if (skipCount > MAX_SKIP) {
            throw new SkipLimitExceededException(MAX_SKIP, t);
        }

        var error = new ErrorPolicy(stepExecution.getCommitCount(),
                stepExecution.getFailureExceptions().getFirst().getMessage(),
                stepExecution.getStepName(), skipCount, LocalDate.now().toString());
        errorPort.insert(error);
        return true;
    }
}
