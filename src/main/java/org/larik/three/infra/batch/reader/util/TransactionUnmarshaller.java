package org.larik.three.infra.batch.reader.util;

import org.larik.three.domain.enums.PaymentStatus;
import org.larik.three.domain.model.Client;
import org.larik.three.domain.model.Payment;
import org.larik.three.domain.model.Transaction;
import org.larik.three.domain.dto.TransactionXml;
import org.springframework.batch.infrastructure.item.ResourceAware;
import org.springframework.core.io.Resource;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

import javax.xml.transform.Source;

@Component
public class TransactionUnmarshaller extends Jaxb2Marshaller implements ResourceAware {

    private String fileName;

    public TransactionUnmarshaller() {
        setClassesToBeBound(TransactionXml.class);
    }

    @Override
    public Object unmarshal(Source source) throws XmlMappingException {
        TransactionXml raw = (TransactionXml) super.unmarshal(source);

        return Transaction.builder()
                .transactionId(null)
                .client(Client.builder()
                        .clientId(raw.getClientId())
                        .name(raw.getClientName())
                        .build())

                .payment(Payment.builder()
                        .value(raw.getPaymentValue())
                        .date(raw.getPaymentDate())
                        .status(PaymentStatus.getByLocalName(raw.getPaymentStatus()))
                        .build())

                .fileOrigin(fileName)
                .build();

    }

    @Override
    public void setResource(Resource resource) {
        this.fileName = resource.getFilename();
    }
}
