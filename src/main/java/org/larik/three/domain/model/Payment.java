package org.larik.three.domain.model;

import lombok.Data;
import org.larik.three.domain.enums.PaymentStatus;

import java.time.LocalDate;

@Data
public class Payment {

    private String paymentValue;

    private LocalDate paymentDate;

    private PaymentStatus paymentStatus;

}
