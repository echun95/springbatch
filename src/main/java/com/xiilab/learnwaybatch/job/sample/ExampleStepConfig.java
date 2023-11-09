package com.xiilab.learnwaybatch.job.sample;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class ExampleStepConfig {

    public static final String STEP_NAME = ExampleJobConfig.JOB_NAME + ".EXAMPLE_STEP";
    private final ExampleJobParameter jobParameter;

    @Bean(STEP_NAME)
    @JobScope
    public Step exampleStep(
            JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<String, String>chunk(jobParameter.getChunkSize(), transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<String> reader() {
        return new ListItemReader<>(getTests());
    }

    @Bean
    public ItemProcessor<String, String> processor() {
        return item -> item.toUpperCase(); // 간단한 처리 예시
    }

    @Bean
    public ItemWriter<String> writer() {
        return items -> {
            for (String item : items) {
                System.out.println("현재시각 : " + LocalDateTime.now());
                System.out.println("JobParam : " + jobParameter.getDatetime());
                System.out.println("Processed: " + item);
            }
        };
    }
    private List<String> getTests() {
        return List.of("a");
    }
}