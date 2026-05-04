package org.larik.three.domain.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;

public record CompleteReport(@JsonUnwrapped AbstractReport report, ComparisonTransactionResult.ComparisonStatus status) implements Report {
}
