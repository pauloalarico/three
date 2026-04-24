package org.larik.three.domain.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Client {

    private Long clientId;

    private String name;
}
