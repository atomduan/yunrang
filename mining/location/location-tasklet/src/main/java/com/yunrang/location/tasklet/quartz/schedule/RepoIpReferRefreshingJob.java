package com.yunrang.location.tasklet.quartz.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunrang.location.integration.routine.RoutineRepoIpReferRefreshing;
import com.yunrang.location.tasklet.util.UtilLocationTaskletContextSingaletonFactory;


/**
 * scheduler server : 10.21.130.34  --> mysql server: 10.21.129.40:3306
 */
public class RepoIpReferRefreshingJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(RepoIpReferRefreshingJob.class);
	public static final String CRON = "0 0 15 ? * WED";
	
	public void execute(JobExecutionContext context) {
		logger.info("RepoIpReferRefreshing begin....: ");
    	try {
    		RoutineRepoIpReferRefreshing routineRepoIpReferRefreshing = UtilLocationTaskletContextSingaletonFactory.getInstance().
    			getBean("routineRepoIpReferRefreshing", RoutineRepoIpReferRefreshing.class);
    		routineRepoIpReferRefreshing.doRountine();
			logger.info("RepoIpReferRefreshing complete....: ");
        } catch (Exception e) {
            logger.error("RepoIpReferRefreshing error......", e);
        }
	}
}