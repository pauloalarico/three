package org.larik.three.infra.batch.job;

import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.larik.three.domain.model.CompleteReport;
import org.larik.three.domain.model.DivergentDataReport;
import org.larik.three.domain.model.FinancialPositionReport;
import org.larik.three.domain.model.ProcessedTransaction;
import org.larik.three.infra.batch.processor.report.CompleteDataProcessor;
import org.larik.three.infra.batch.processor.report.DivergentDataProcessor;
import org.larik.three.infra.batch.processor.report.FinancialReportProcessor;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class FinalReportStepConfig {

    @Bean
    public Flow flow(Step completeReport, Step divergentDataReport, Step financialReport, TaskExecutor taskExecutor) {
        Flow completeReportFlow = new FlowBuilder<SimpleFlow>("completeReportFlow").start(completeReport).build();
        Flow divergentReportFlow = new FlowBuilder<SimpleFlow>("divergentReportFlow").start(divergentDataReport).build();
        Flow financialReportFlow = new FlowBuilder<SimpleFlow>("financialReportFlow").start(financialReport).build();

        return new FlowBuilder<SimpleFlow>("parallelFlow")
                .split(taskExecutor)
                .add(completeReportFlow, divergentReportFlow, financialReportFlow)
                .build();
    }

    @Bean
    public Step parallelReportStep(JobRepository jobRepository, Flow flow) {
        return new StepBuilder("report-writing", jobRepository)
                .flow(flow)
                .build();
    }

    @Bean
    public Step completeReport(JobRepository jobRepository,
                               ItemReader<ComparisonTransactionResult> completeTransactionsReader,
                               CompleteDataProcessor processor,
                               ItemWriter<CompleteReport> writer) {
        return new StepBuilder("completeReportStep", jobRepository)
                .<ComparisonTransactionResult, CompleteReport>chunk(15)
                .reader(completeTransactionsReader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step divergentDataReport(JobRepository jobRepository,
                               ItemReader<ComparisonTransactionResult> divergentDataReader,
                               DivergentDataProcessor processor,
                               ItemWriter<DivergentDataReport> writer) {
        return new StepBuilder("divergentDataReportStep", jobRepository)
                .<ComparisonTransactionResult, DivergentDataReport>chunk(15)
                .reader(divergentDataReader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step financialReport(JobRepository jobRepository,
                                    ItemReader<ProcessedTransaction> financialProjectionReader,
                                    FinancialReportProcessor processor,
                                    ItemWriter<FinancialPositionReport> writer) {
        return new StepBuilder("financialReportStep", jobRepository)
                .<ProcessedTransaction, FinancialPositionReport>chunk(15)
                .reader(financialProjectionReader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
