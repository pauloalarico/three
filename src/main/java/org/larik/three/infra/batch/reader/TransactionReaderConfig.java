package org.larik.three.infra.batch.reader;

import org.larik.three.domain.model.Transaction;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectReader;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.batch.infrastructure.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;
import org.springframework.batch.infrastructure.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class TransactionReaderConfig {

    @Bean
    public FlatFileItemReader<Transaction> csvFileReader() {
        return new FlatFileItemReaderBuilder<Transaction>()
                .name("csvFileReader")
                .resource(new FileSystemResource("/.data/banco_a_transacoes.csv"))
                .linesToSkip(1)
                .targetType(Transaction.class)
                .delimited()
                .delimiter(",")
                .names("id", "clientId", "clientName", "value", "dueDate", "status")
                .build();
    }

    @Bean
    public JsonItemReader<Transaction> jsonFileReader() {
        return new JsonItemReaderBuilder<Transaction>()
                .name("jsonFileReader")
                .resource(new FileSystemResource("/.data/banco_b_transacoes.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(Transaction.class))
                .build();
    }

    @Bean
    public StaxEventItemReader<Transaction> xmlFileReader() {
        return new StaxEventItemReaderBuilder<Transaction>()
                .name("xmlFileReader")
                .resource(new FileSystemResource("/.data/operadora_cartao_transacoes.xml"))
                .addFragmentRootElements("Transacao")
                .unmarshaller()
                .build();
    }

}
