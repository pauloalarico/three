package org.larik.three.domain.model;

import java.math.BigDecimal;

public record RawRemittance(String date, BigDecimal value) implements RemittanceData {
}
