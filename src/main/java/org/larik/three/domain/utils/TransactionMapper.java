package org.larik.three.domain.utils;

import org.larik.three.domain.enums.PaymentStatus;
import org.larik.three.domain.model.Client;
import org.larik.three.domain.model.Payment;
import org.larik.three.domain.model.Transaction;
import org.larik.three.domain.valueobject.TransactionTrustSource;

import java.math.RoundingMode;

public class TransactionMapper {

    public static Transaction toTransaction(TransactionTrustSource valueObject) {

        return Transaction.builder().
                transactionId(null)
                .client(
                        Client.builder()
                                .clientId(valueObject.clientId())
                                .name(valueObject.clientName())
                                .build())
                .payment(
                        Payment.builder()
                                .value(valueObject.amount().setScale(2, RoundingMode.HALF_UP))
                                .date(removeTime(valueObject.date()))
                                .status(PaymentStatus.getByLocalName(valueObject.status()))
                                .build()
                )
                .fileOrigin(null)
                .build();

    }

    private static String removeTime(String date) {
        return date.split("T")[0];
    }
}
