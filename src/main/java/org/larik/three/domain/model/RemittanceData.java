package org.larik.three.domain.model;

import java.math.BigDecimal;

public sealed interface RemittanceData permits RawRemittance, ExpectedRemittance{
    String date();
    BigDecimal value();
}
