package org.larik.three.domain.enums;

import lombok.*;

import java.util.HashMap;

@RequiredArgsConstructor
public enum PaymentStatus {
    PAID("APROVADA", "PAID"),
    PENDENT("PENDENTE", "PENDENT"),
    CANCELED("RECUSADA", "CANCELED");

    private final String localName;

    private final String globalName;

    private final static HashMap<String, PaymentStatus> BY_LOCAL_NAME = new HashMap<>();

    static {
        for (PaymentStatus e : values()) {
            BY_LOCAL_NAME.put(e.localName, e);
        }
    }

    public static PaymentStatus getByLocalName(String status) {
        PaymentStatus constant = BY_LOCAL_NAME.get(status);
        if(constant == null) {
            throw new IllegalArgumentException("Enum not found");
        }

        return constant;
    }

}
