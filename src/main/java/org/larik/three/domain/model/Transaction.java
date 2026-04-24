package org.larik.three.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"transactionId", "fileOrigin"})
public class Transaction {

    @Id
    private String transactionId;

    private Client client;

    private Payment payment;

    private String fileOrigin;
}
