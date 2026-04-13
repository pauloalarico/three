package org.larik.three.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Transaction {

    @Id
    private String transactionId;

    private Client client;

    private Payment payment;

    private String fileOrigin;
}
