package org.larik.three.domain.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.math.BigDecimal;

public record DivergentDataReport(@JsonUnwrapped AbstractReport report, BigDecimal delta) implements Report{
}
