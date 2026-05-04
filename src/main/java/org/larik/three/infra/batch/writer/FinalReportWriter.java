package org.larik.three.infra.batch.writer;

import org.larik.three.domain.model.CompleteReport;
import org.larik.three.domain.model.DivergentDataReport;
import org.larik.three.domain.model.FinancialPositionReport;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class FinalReportWriter {

    @Bean
    public FlatFileItemWriter<CompleteReport> generalWriter() {
        return new FlatFileItemWriterBuilder<CompleteReport>()
                .name("generalFileWriter")
                .resource(new FileSystemResource("output/general.csv"))
                .lineAggregator(aggregateLineCompleteReport())
                .headerCallback(h -> h.write("transaction_id, client_id, client_name, expected_date, raw_value, expected_value, status"))
                .build();
    }

    @Bean
    public FlatFileItemWriter<DivergentDataReport> divergentWriter() {
        return new FlatFileItemWriterBuilder<DivergentDataReport>()
                .name("divergentFileWriter")
                .resource(new FileSystemResource("output/divergent.csv"))
                .lineAggregator(aggregateLineDivergentData())
                .headerCallback(h -> h.write("transaction_id, client_id, client_name, expected_date, raw_value, expected_value, delta"))
                .build();
    }

    @Bean
    public FlatFileItemWriter<FinancialPositionReport> financialReportWriter() {
        return new FlatFileItemWriterBuilder<FinancialPositionReport>()
                .name("financialFileWriter")
                .resource(new FileSystemResource("output/financial.csv"))
                .lineAggregator(aggregateLineFinancialPosition())
                .headerCallback(h -> h.write("reference_date, expected_value, received_value, difference_value, first_month_project, second_month_value, third_month_projection"))
                .build();
    }

    private DelimitedLineAggregator<CompleteReport> aggregateLineCompleteReport() {
        DelimitedLineAggregator<CompleteReport> aggregator = new DelimitedLineAggregator<>();
        aggregator.setDelimiter(",");
        aggregator.setFieldExtractor(item -> new Object[] {
                item.transactionId(),
                item.clientId(),
                item.clientName(),
                item.expectedDate(),
                item.rawValue(),
                item.expectedValue(),
                item.status()
        });

        return aggregator;
    }

    private DelimitedLineAggregator<DivergentDataReport> aggregateLineDivergentData() {
        DelimitedLineAggregator<DivergentDataReport> aggregator = new DelimitedLineAggregator<>();
        aggregator.setDelimiter(",");
        aggregator.setFieldExtractor(item -> new Object[] {
                item.transactionId(),
                item.clientId(),
                item.clientName(),
                item.expectedDate(),
                item.rawValue(),
                item.expectedValue(),
                item.delta()
        });

        return aggregator;
    }

    private DelimitedLineAggregator<FinancialPositionReport> aggregateLineFinancialPosition() {
        DelimitedLineAggregator<FinancialPositionReport> aggregator = new DelimitedLineAggregator<>();
        aggregator.setDelimiter(",");
        aggregator.setFieldExtractor(item -> new Object[] {
                item.referenceDate(),
                item.expectedValue(),
                item.receivedValue(),
                item.differenceValue(),
                item.firstMonthProjection(),
                item.secondMonthProjection(),
                item.thirdMonthProjection()
        });

        return aggregator;
    }

}
