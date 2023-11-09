package com.xiilab.learnwaybatch.job.sample;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JobScope
@Component
public class ExampleJobParameter {

    private String datetime;

    @Value("${chunk-size:1000}")
    private int chunkSize;

    @Value("#{jobParameters[datetime]}")
    public void setDate(String datetime) {
        this.datetime = datetime;
    }
}