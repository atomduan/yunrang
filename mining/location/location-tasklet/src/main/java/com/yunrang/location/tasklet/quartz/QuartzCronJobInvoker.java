package com.yunrang.location.tasklet.quartz;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunrang.location.tasklet.quartz.schedule.RepoIpRangeRefreshingJob;
import com.yunrang.location.tasklet.quartz.schedule.RepoIpReferRefreshingJob;


public class QuartzCronJobInvoker {
	private static final Logger logger = LoggerFactory.getLogger(QuartzCronJobInvoker.class);
	private static final SchedulerFactory schedulerFactory = new StdSchedulerFactory();
	
	public static void schedule(String jobName, Class<? extends Job> jobClass, String cornExp) {
		try {
	        Scheduler scheduler = schedulerFactory.getScheduler();
	        JobDetail jobDetail = new JobDetail(jobName, jobClass);
	        CronTrigger cronTrigger = new CronTrigger();
	        try {
	            CronExpression cexp = new CronExpression(cornExp);
	            cronTrigger.setCronExpression(cexp);
	            cronTrigger.setName(jobClass.getSimpleName());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        scheduler.scheduleJob(jobDetail, cronTrigger);
	        scheduler.start();
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
		}
	}
	
	public static void main(String[] args) {
		String taskName = args[0];
		if (taskName.equalsIgnoreCase("RepoIpRangeRefreshing")) {
			schedule("repoIpRangeRefreshing", RepoIpRangeRefreshingJob.class, RepoIpReferRefreshingJob.CRON);
		}
		if (taskName.equalsIgnoreCase("RepoIpReferRefreshing")) {
			schedule("repoIpReferRefreshing", RepoIpReferRefreshingJob.class, RepoIpReferRefreshingJob.CRON);
		}
	}
}
