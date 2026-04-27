package org.larik.three.domain.model;

import lombok.Builder;
import lombok.Data;
import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
public class ProcessedTransaction {

    @Id
    private String id;

    private Client client;

    private ComparisonTransactionResult.ComparisonStatus status;

    private Remittance remittance;

    private Balance balance;

}
