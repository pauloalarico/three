package org.larik.three.infra.batch.reader;

import org.larik.three.domain.model.Transaction;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class TransactionXmlMarshaller {

    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(Transaction.class);
        return jaxb2Marshaller;
    }
}
