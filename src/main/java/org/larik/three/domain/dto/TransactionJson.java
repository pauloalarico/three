package org.larik.three.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;


public record TransactionJson (

        @JsonProperty("clientId")
        Long clientId,

        @JsonProperty("clientName")
        String clientName,

        @JsonProperty("amount")
        BigDecimal paymentValue,

        @JsonProperty("date")
        String paymentDate,

        @JsonProperty("status")
        String paymentStatus
) {
}
