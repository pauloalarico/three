package org.larik.three.infra.batch.reader;

import jakarta.annotation.PostConstruct;
import org.jspecify.annotations.Nullable;
import org.larik.three.domain.model.Transaction;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamException;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class TransactionAsyncReader implements ItemStreamReader<Transaction> {

    private final MultiResourceItemReader<Transaction> csvReader;

    private final MultiResourceItemReader<Transaction> jsonReader;

    private final MultiResourceItemReader<Transaction> xmlReader;

    private ItemStreamReader<Transaction> delegate;

    @Value("#{stepExecutionContext['format']}")
    private String fileFormat;

    public TransactionAsyncReader(MultiResourceItemReader<Transaction> csvReader, MultiResourceItemReader<Transaction> jsonReader, MultiResourceItemReader<Transaction> xmlReader) {
        this.csvReader = csvReader;
        this.jsonReader = jsonReader;
        this.xmlReader = xmlReader;
    }

    @PostConstruct
    public void init() {
        this.delegate = switch (fileFormat) {
            case "CSV" -> csvReader;
            case "JSON" -> jsonReader;
            case "XML" -> xmlReader;
            default -> throw new IllegalArgumentException("File not supported for thread processing");
        };
    }

    @Override
    public @Nullable Transaction read() throws Exception {
        return delegate.read();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }
}
