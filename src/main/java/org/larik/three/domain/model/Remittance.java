package org.larik.three.domain.model;

public record Remittance(
    ExpectedRemittance expected,
    RawRemittance raw) {
}
