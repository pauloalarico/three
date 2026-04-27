package org.larik.three.domain.model;

public record Remittance(
        RawRemittance raw,
        ExpectedRemittance expected) {
}
