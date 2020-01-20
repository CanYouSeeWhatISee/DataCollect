package com.turingcat.datacollect.Job;

import com.turingcat.datacollect.Service.DataCollectService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class MatchJob implements Job {
    @Autowired
    private DataCollectService dataCollectService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        dataCollectService.matchData();
    }
}
