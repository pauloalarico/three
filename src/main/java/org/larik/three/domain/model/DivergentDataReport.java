package org.larik.three.domain.model;

import java.math.BigDecimal;

public record DivergentDataReport(AbstractReport report, BigDecimal delta) implements Report{
}
