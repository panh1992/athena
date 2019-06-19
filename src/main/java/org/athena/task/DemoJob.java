package org.athena.task;

import org.athena.config.quartz.Scheduled;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@Scheduled(cron = "0 0 1/1 * * ? ", jobName = "演示任务")
@DisallowConcurrentExecution
public class DemoJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(DemoJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("quartz job 测试， 调度时间：{}", Instant.now());
    }

}