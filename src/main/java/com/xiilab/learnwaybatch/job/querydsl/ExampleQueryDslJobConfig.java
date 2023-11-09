package com.xiilab.learnwaybatch.job.querydsl;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExampleQueryDslJobConfig {

    public static final String JOB_NAME = "EXAMPLE_QUERYDSL_JOB";
    private final Step exampleStep;

    public ExampleQueryDslJobConfig(@Qualifier(ExampleQueryDslStepConfig.STEP_NAME) Step exampleStep) {
        this.exampleStep = exampleStep;
    }
    @Bean(JOB_NAME)
    public Job exampleQueryDslJob(JobRepository jobRepository) {
        return new JobBuilder("aaa", jobRepository)
                .start(exampleStep)
                .build();
    }
}