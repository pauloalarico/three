package org.larik.three.domain.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ExpectedRemittance(String date, BigDecimal value) implements RemittanceData {
}
