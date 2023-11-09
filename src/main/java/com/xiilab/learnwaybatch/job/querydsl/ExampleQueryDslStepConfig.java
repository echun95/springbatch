package com.xiilab.learnwaybatch.job.querydsl;

import static com.xiilab.learnwaybatch.entity.QTest.*;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.xiilab.learnwaybatch.entity.Test;
import com.xiilab.learnwaybatch.job.reader.QuerydslPagingItemReader;
import com.xiilab.learnwaybatch.job.sample.ExampleJobParameter;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class ExampleQueryDslStepConfig {

    public static final String STEP_NAME = ExampleQueryDslJobConfig.JOB_NAME + ".EXAMPLE_QUERYDSL_STEP";
    private final ExampleJobParameter jobParameter;
    private final EntityManagerFactory emf;

    @Bean(STEP_NAME)
    @JobScope
    public Step exampleStep(
            JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Test, Test>chunk(2, transactionManager)
                .reader(exampleQueryDslReader())
                .processor(exampleQueryDslProcessor())
                .writer(exampleQueryDslWriter())
                .build();
    }
    @Bean
    @StepScope
    public QuerydslPagingItemReader<Test> exampleQueryDslReader() {
        return new QuerydslPagingItemReader<>(emf, jobParameter.getChunkSize(),
            jpaQueryFactory -> jpaQueryFactory.selectFrom(test)
            );
    }

    @Bean
    public ItemProcessor<Test, Test> exampleQueryDslProcessor() {
        return test -> {
            System.out.println("id : " + test.getId() + ", cnt : " + test.getTestCount());
            test.addCount();
            return test;
        };
    }

    @Bean
    public JpaItemWriter<Test> exampleQueryDslWriter() throws Exception {
        return new JpaItemWriterBuilder<Test>()
            .entityManagerFactory(this.emf)
            .build();
    }
}