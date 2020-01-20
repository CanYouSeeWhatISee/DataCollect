package com.turingcat.datacollect.Service;

import com.turingcat.datacollect.Job.CollectJob;
import com.turingcat.datacollect.Job.MatchJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * 定时任务
 * 每30分钟执行一次
 */
@Component
public class QuartzService {

    @Autowired
    private Scheduler scheduler;

    //在@PostConstruct之前执行
    @Bean
    public Scheduler scheduler() throws SchedulerException {
        SchedulerFactory schedulerFactoryBean = new StdSchedulerFactory();
        return schedulerFactoryBean.getScheduler();
    }

    @PostConstruct
    public void init() throws SchedulerException{
        System.err.println("执行");
        //每30分钟运行一次MyJob
//        String cron = "0/10 * * * * ? ";
        String cron = "0 1,31 * * * ? ";
//        String cron = "*/5 * * * * ?";
        String cronMatch = "0 0 0 */1 * ?";
        addJob(cron, CollectJob.class,"key");
        addJob(cronMatch, MatchJob.class,"key1");
        scheduler.start();

    }

    private void addJob(String cron, Class<? extends Job> clazz, String jobId) throws SchedulerException {
        //创建一个jobDetail的实例，将该实例与目标Job Class绑定
        JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(JobKey.jobKey(jobId)).build();
        //cron表达式
        TriggerBuilder<Trigger> builder = TriggerBuilder.newTrigger()
                .withIdentity(TriggerKey.triggerKey(jobId));
        //现在执行
        builder.startAt(new Date());
        Trigger trigger = builder.withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        //创建schedule实例
        scheduler.scheduleJob(jobDetail,trigger);
    }

}
