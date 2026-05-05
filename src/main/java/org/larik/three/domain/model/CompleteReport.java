package org.larik.three.domain.model;

import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;

public record CompleteReport(AbstractReport report, ComparisonTransactionResult.ComparisonStatus status) implements Report {
}
