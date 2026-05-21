package org.larik.three.domain.port.out;

import org.larik.three.domain.model.ErrorPolicy;

public interface ErrorPort {

    void insert(ErrorPolicy e);
}
