package com.turingcat.datacollect;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class DatacollectApplication {

    public static void main(String[] args){
        SpringApplication.run(DatacollectApplication.class, args);
    }

}
