package com.xiilab.learnwaybatch.job;

import java.time.LocalDateTime;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuartzJobLauncher extends QuartzJobBean {
	private String jobName;
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	protected void executeInternal(JobExecutionContext context) {
		Job job = (Job)applicationContext.getBean(jobName);
		JobParameters datetime = new JobParametersBuilder()
			.addString("datetime", LocalDateTime.now().toString())
			.toJobParameters();
		try {
			this.jobLauncher.run(job, datetime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
