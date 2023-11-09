package com.xiilab.learnwaybatch.config.quartz;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.xiilab.learnwaybatch.job.QuartzJobLauncher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class QuartzConfig {
    private final DataSource dataSource;
    private final ApplicationContext applicationContext;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public JobDetail sampleJobDetail() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", "EXAMPLE_JOB");
        return createJobDetail(QuartzJobLauncher.class, "sampleJobDetail", jobDataMap);
    }

    @Bean
    public Trigger sampleJobTrigger() {
        return createCronTrigger(sampleJobDetail(),"sampleJobTrigger","0/10 * * * * ?");
    }
    @Bean
    public JobDetail sampleQueryDslJobDetail() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", "EXAMPLE_QUERYDSL_JOB");
        return createJobDetail(QuartzJobLauncher.class, "sampleQueryDslJobDetail", jobDataMap);
    }

    @Bean
    public Trigger sampleQueryDslJobTrigger() {
        return createCronTrigger(sampleQueryDslJobDetail(),"sampleQueryDslJobTrigger","0/10 * * * * ?");
    }

    // 메서드를 사용하여 JobDetail 객체 생성
    private JobDetail createJobDetail(Class<? extends QuartzJobBean> jobClass, String jobName,JobDataMap jobDataMap) {
        return JobBuilder.newJob(jobClass)
            .withIdentity(jobName)
            .setJobData(jobDataMap)
            .storeDurably()
            .build();
    }

    // 메서드를 사용하여 CronTrigger 객체 생성
    private Trigger createCronTrigger(JobDetail jobDetail, String triggerName, String cronExpression) {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(triggerName)
            .withSchedule(cronScheduleBuilder)
            .build();
    }


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        AutoWiringSpringBeanJobFactory autoWiringSpringBeanJobFactory = new AutoWiringSpringBeanJobFactory();
        autoWiringSpringBeanJobFactory.setApplicationContext(applicationContext);

        schedulerFactoryBean.setJobFactory(autoWiringSpringBeanJobFactory);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setTransactionManager(platformTransactionManager);
        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        schedulerFactoryBean.setJobDetails(sampleQueryDslJobDetail());
        schedulerFactoryBean.setTriggers(sampleQueryDslJobTrigger());
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        return schedulerFactoryBean;
    }

    private Properties quartzProperties() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        Properties properties = null;
        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();
        } catch (IOException e) {
            log.error("quartzProperties parse error : {}", e);
        }
        return properties;
    }
}
