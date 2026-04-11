package org.larik.three.domain.model;

import org.larik.three.domain.enums.PaymentStatus;

import java.time.LocalDate;

public class Payment {

    private String paymentValue;

    private LocalDate paymentDate;

    private PaymentStatus paymentStatus;

}
