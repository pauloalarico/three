package org.larik.three.infra.batch.reader;

import org.jspecify.annotations.Nullable;
import org.larik.three.domain.model.Transaction;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamException;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;

public class TransactionReader implements ItemStreamReader<Transaction> {

    private final FlatFileItemReader<Transaction> csvReader;

    private final JsonItemReader<Transaction> jsonReader;

    private final StaxEventItemReader<Transaction> xmlReader;

    private boolean isCsvFinished = false;

    private boolean isJsonFinished = false;

    private static final String CSV_FINISHED = "transactionReader:isCsvFinished";

    private static final String JSON_FINISHED = "transactionReader:isJsonFinished";

    public TransactionReader(FlatFileItemReader<Transaction> csvReader,
                             JsonItemReader<Transaction> jsonReader,
                             StaxEventItemReader<Transaction> xmlReader) {
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
            jsonReader.open(new ExecutionContext());
        }


        if (!isJsonFinished) {
            var transactionByJson = jsonReader.read();

            if (transactionByJson != null) {
                return transactionByJson;
            }

            isJsonFinished = true;
            jsonReader.close();
            xmlReader.open(new ExecutionContext());
        }

        return xmlReader.read();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
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
