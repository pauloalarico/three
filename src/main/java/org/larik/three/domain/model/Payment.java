package org.larik.three.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.larik.three.domain.enums.PaymentStatus;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    private BigDecimal value;

    private String date;

    private PaymentStatus status;

}
