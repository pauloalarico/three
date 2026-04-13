package org.larik.three.infra.batch.reader.util;

import org.jspecify.annotations.Nullable;
import org.larik.three.domain.dto.TransactionJson;
import org.larik.three.domain.enums.PaymentStatus;
import org.larik.three.domain.model.Client;
import org.larik.three.domain.model.Payment;
import org.larik.three.domain.model.Transaction;
import org.springframework.batch.infrastructure.item.ResourceAware;
import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectReader;
import org.springframework.batch.infrastructure.item.json.JsonObjectReader;
import org.springframework.core.io.Resource;

public class TransactionJsonReader implements JsonObjectReader<Transaction>, ResourceAware {

    private final JacksonJsonObjectReader<TransactionJson> delegate = new JacksonJsonObjectReader<>(TransactionJson.class);

    private String fileName;

    @Override
    public void open(Resource resource) throws Exception {
        delegate.open(resource);
    }

    @Override
    public @Nullable Transaction read() throws Exception {
        TransactionJson json = delegate.read();
        if (json == null) {
            return null;
        }

        return Transaction.builder()
                .transactionId(null)
                .client(Client.builder()
                        .clientId(json.clientId())
                        .name(json.clientName())
                .build())

                .payment(Payment.builder()
                        .value(json.paymentValue())
                        .date(json.paymentDate())
                        .status(PaymentStatus.getByLocalName(json.paymentStatus()))
                        .build())

                .fileOrigin(fileName)
                .build();

    }

    @Override
    public void close() throws Exception {
        delegate.close();
    }

    @Override
    public void setResource(Resource resource) {
        this.fileName = resource.getFilename();
    }
}
