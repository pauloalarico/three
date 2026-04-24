package org.larik.three.domain.model;

import lombok.*;
import org.larik.three.domain.enums.PaymentStatus;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Payment {

    private BigDecimal value;

    private String date;

    private PaymentStatus status;

}
