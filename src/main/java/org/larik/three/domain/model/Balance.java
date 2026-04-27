package org.larik.three.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Balance {

    private BigDecimal realBalance;

    private BigDecimal expectedAmount;

    private BigDecimal balanceDifference;

    private BigDecimal taxBalanceWithMonth;

    private BigDecimal taxBalanceWithTwoMonths;

    private BigDecimal taxBalanceWithThreeMonths;

}
