package org.larik.three.infra.batch.reader.config;

import lombok.RequiredArgsConstructor;
import org.larik.three.domain.model.Transaction;
import org.larik.three.infra.batch.reader.TransactionAsyncReader;
import org.larik.three.infra.batch.reader.TransactionReader;
import org.larik.three.infra.batch.reader.utils.TransactionFieldMapper;
import org.larik.three.infra.batch.reader.utils.TransactionJsonReader;
import org.larik.three.infra.batch.reader.utils.TransactionUnmarshaller;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.batch.infrastructure.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;
import org.springframework.batch.infrastructure.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class TransactionReaderConfig {

    private final ResourcePatternResolver resolver;

    @Bean
    @StepScope
    public TransactionAsyncReader reader() throws IOException {
        //return new TransactionReader(csvFileReader(), jsonFileReader(), xmlFileReader());
        return new TransactionAsyncReader(csvFileReader(), jsonFileReader(), xmlFileReader());
    }

    @Bean
    public MultiResourceItemReader<Transaction> csvFileReader() throws IOException {
        Resource[] resources = resolver.getResources("file:.data/*.csv");
        var file = getFileNameByResource(resources);

        FlatFileItemReader<Transaction> delegate = new FlatFileItemReaderBuilder<Transaction>()
                .name("csvFileDelegate")
                .linesToSkip(1)
                .fieldSetMapper(new TransactionFieldMapper(file))
                .delimited()
                .delimiter(",")
                .names("clientId", "clientName", "value", "date", "status")
                .build();

        return new MultiResourceItemReaderBuilder<Transaction>()
                .name("csvFileReader")
                .resources(resources)
                .delegate(delegate)
                .build();

    }

    @Bean
    public MultiResourceItemReader<Transaction> jsonFileReader() throws IOException {
        Resource[] resources = resolver.getResources("file:.data/*.json");
        var fileName = getFileNameByResource(resources);

        JsonItemReader<Transaction> delegate = new JsonItemReaderBuilder<Transaction>()
                .name("jsonFileDelegate")
                .jsonObjectReader(new TransactionJsonReader(fileName))
                .build();

        return new MultiResourceItemReaderBuilder<Transaction>()
                .name("jsonFileReader")
                .resources(resources)
                .delegate(delegate)
                .build();
    }

    @Bean
    public MultiResourceItemReader<Transaction> xmlFileReader() throws IOException {
        Resource[] resources = resolver.getResources("file:.data/*.xml");
        var fileName = getFileNameByResource(resources);

        StaxEventItemReader<Transaction> delegate = new StaxEventItemReaderBuilder<Transaction>()
                .name("xmlFileDelegate")
                .addFragmentRootElements("Transacao")
                .unmarshaller(new TransactionUnmarshaller(fileName))
                .build();

        return new MultiResourceItemReaderBuilder<Transaction>()
                .name("xmlFileReader")
                .resources(resources)
                .delegate(delegate)
                .build();
    }

    private String getFileNameByResource(Resource [] resources) {
        return Arrays.stream(resources).findFirst().orElseThrow(() -> new RuntimeException("not a file")).getFilename();
    }

}
