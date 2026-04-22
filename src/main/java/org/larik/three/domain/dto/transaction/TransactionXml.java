    package org.larik.three.domain.dto.transaction;

    import jakarta.xml.bind.annotation.XmlAccessType;
    import jakarta.xml.bind.annotation.XmlAccessorType;
    import jakarta.xml.bind.annotation.XmlElement;
    import jakarta.xml.bind.annotation.XmlRootElement;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.math.BigDecimal;

    @XmlRootElement(name = "Transacao")
    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class TransactionXml {

        @XmlElement(name = "ClienteCodigo")
        private Long clientId;

        @XmlElement(name = "ClienteNome")
        private String clientName;

        @XmlElement(name = "Valor")
        private BigDecimal paymentValue;

        @XmlElement(name = "Data")
        private String paymentDate;

        @XmlElement(name = "Situacao")
        String paymentStatus;
    }
