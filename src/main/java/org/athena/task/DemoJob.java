package org.athena.task;

import io.dropwizard.jobs.Job;
import io.dropwizard.jobs.annotations.On;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@On(value = "0 0/50 * * * ? ", jobName = "演示任务")
@DisallowConcurrentExecution
public class DemoJob extends Job {

    private static Logger logger = LoggerFactory.getLogger(DemoJob.class);

    @Override
    public void doJob(JobExecutionContext context) {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.info("quartz job 测试， 调度时间：{}", Instant.now());
    }

}
