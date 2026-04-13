package org.larik.three.infra.batch.reader;

import org.jspecify.annotations.Nullable;
import org.larik.three.domain.model.Transaction;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamException;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;

public class TransactionReader implements ItemStreamReader<Transaction> {

    private final MultiResourceItemReader<Transaction> csvReader;

    private final MultiResourceItemReader<Transaction> jsonReader;

    private final MultiResourceItemReader<Transaction> xmlReader;

    private ExecutionContext executionContext;

    private boolean isCsvFinished = false;

    private boolean isJsonFinished = false;

    private static final String CSV_FINISHED = "transactionReader:isCsvFinished";

    private static final String JSON_FINISHED = "transactionReader:isJsonFinished";

    public TransactionReader(MultiResourceItemReader<Transaction> csvReader,
                             MultiResourceItemReader<Transaction> jsonReader,
                             MultiResourceItemReader<Transaction> xmlReader) {
        this.csvReader = csvReader;
        this.jsonReader = jsonReader;
        this.xmlReader = xmlReader;
    }

    @Override
    public @Nullable Transaction read() throws Exception {

        if(!isCsvFinished) {
            var transaction = csvReader.read();

            if (transaction != null) {
                return transaction;
            }

            isCsvFinished = true;
            csvReader.close();
            jsonReader.open(executionContext);
        }


        if (!isJsonFinished) {
            var transactionByJson = jsonReader.read();

            if (transactionByJson != null) {
                return transactionByJson;
            }

            isJsonFinished = true;
            jsonReader.close();
            xmlReader.open(executionContext);
        }

        return xmlReader.read();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.executionContext = executionContext;
        this.isCsvFinished = executionContext.getInt(CSV_FINISHED, 0) == 1;
        this.isJsonFinished = executionContext.getInt(JSON_FINISHED, 0) == 1;

        if (!isCsvFinished) {
            csvReader.open(executionContext);
        }

        else if (!isJsonFinished) {
            jsonReader.open(executionContext);
        }

        else {
            xmlReader.open(executionContext);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.put(CSV_FINISHED, isCsvFinished ? 1 : 0);
        executionContext.put(JSON_FINISHED, isJsonFinished ? 1 : 0);

        if (!isCsvFinished) {
            csvReader.update(executionContext);
        }

        else if (!isJsonFinished) {
            jsonReader.update(executionContext);
        }

        else {
            xmlReader.update(executionContext);
        }
    }

    @Override
    public void close() throws ItemStreamException {
        if (!isCsvFinished) {
            csvReader.close();
        }

        else if (!isJsonFinished) {
            jsonReader.close();
        }

        else {
            xmlReader.close();
        }
    }
}
