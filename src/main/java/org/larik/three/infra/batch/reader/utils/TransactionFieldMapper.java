package org.larik.three.infra.batch.reader.utils;

import lombok.RequiredArgsConstructor;
import org.larik.three.domain.enums.PaymentStatus;
import org.larik.three.domain.model.Client;
import org.larik.three.domain.model.Payment;
import org.larik.three.domain.model.Transaction;
import org.springframework.batch.infrastructure.item.file.mapping.FieldSetMapper;
import org.springframework.batch.infrastructure.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

@RequiredArgsConstructor
public class TransactionFieldMapper implements FieldSetMapper<Transaction> {

    private final String fileName;

    @Override
    public Transaction mapFieldSet(FieldSet fieldSet) throws BindException {
        return Transaction.builder()
                .transactionId(null)
                .client(Client.builder()
                        .clientId(fieldSet.readLong("clientId"))
                        .name(fieldSet.readString("clientName"))
                        .build())

                .payment(Payment.builder()
                        .value(fieldSet.readBigDecimal("value"))
                        .date(fieldSet.readString("date"))
                        .status(PaymentStatus.getByLocalName(fieldSet.readString("status")))
                        .build())

                .fileOrigin(fileName)
                .build();
    }

}
