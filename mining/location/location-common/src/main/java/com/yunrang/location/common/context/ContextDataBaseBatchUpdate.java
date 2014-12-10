package com.yunrang.location.common.context;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class ContextDataBaseBatchUpdate<T> {
	static final Logger logger = LoggerFactory.getLogger(ContextDataBaseBatchUpdate.class);
    
	private Long totalProcessedTuples = 0L;
	private Integer retryCountDown = 72;
    
    private int processOneBatchAndReturnCurrnetPrimaryId(List<T> fetchedResultList) {
        int beginPrimaryId = 0;
        for (int i=0; i<fetchedResultList.size(); i++) {
        	int currentPrimaryId = 0;
        	T ird = fetchedResultList.get(i);
           	currentPrimaryId = processOneIpSampleAndReturnCurrentProcessPrimaryId(ird, fetchedResultList, i);
            if (currentPrimaryId > 0L) {
                beginPrimaryId = currentPrimaryId; 
            } else {
            	logger.warn("currentPrimaryId fetch fail~! the beginPrimaryId will auto-increated to ["+beginPrimaryId+"]!");
            	beginPrimaryId += 1;
            }
            if ((++totalProcessedTuples%100) == 0) {
            	logger.info("totalProcessedTuples:"+totalProcessedTuples+" has been finished");
            }
        }
        logger.info("one batch finished, beginPrimaryId : " + beginPrimaryId);
        return beginPrimaryId;
    }

	public void doProcess() throws Exception {
        int beginPrimaryId = 0;
        List<T> resultList = null;
        for (;;) {
            try {
                resultList = fetchBatchTuplesAscByPrimaryId(beginPrimaryId);
                if (resultList.size() > 0) {
                	beginPrimaryId = processOneBatchAndReturnCurrnetPrimaryId(resultList);
                } else {
                    break;
                }
            } catch (Exception e) {
                logger.error("AbstractMysqlBatchUpdateRoutine DB connection problem .....!", e);
                if (--retryCountDown < 0) {
                    logger.error("ALL quotas have been used routine closed...", e);
                    break;
                } else {
                    logger.error(retryCountDown+" retry quotas left...wait for an hour to retry....", e);
                    Thread.sleep(3600*1000);
                }
            }
        }
    }
	
	public abstract List<T> fetchBatchTuplesAscByPrimaryId(int beginPrimaryId) throws Exception;
	public abstract int processOneIpSampleAndReturnCurrentProcessPrimaryId(T ird, List<T> fetchedResultList, int currIndex);
}
