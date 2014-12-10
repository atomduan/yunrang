package com.yunrang.location.integration.routine;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunrang.location.common.bean.ProfileCity;
import com.yunrang.location.common.context.ContextDataBaseBatchUpdate;
import com.yunrang.location.common.util.UtilIpConverter;
import com.yunrang.location.datamgr.mysql.dao.RepoIpRangeDao;
import com.yunrang.location.datamgr.mysql.dao.RepoIpReferDao;
import com.yunrang.location.datamgr.mysql.model.RepoIpRangeDo;
import com.yunrang.location.datamgr.mysql.model.RepoIpReferDo;
import com.yunrang.location.datamgr.mysql.util.UtilBeanToDoConverter;
import com.yunrang.location.integration.support.api.ip.LocationIpApiAliyun;

public class RoutineRepoIpReferRefreshing {
	private static final Logger logger = LoggerFactory.getLogger(RoutineRepoIpReferRefreshing.class);
    
    private LocationIpApiAliyun locationIpApiAliyun;
    private RepoIpReferDao repoIpReferDao;
    private RepoIpRangeDao repoIpRangeDao;
    
    public void doRountine() throws Exception {
    	logger.info("RoutineRepoIpReferRefreshing-doRoutine begin......");
    	new ContextDataBaseBatchUpdate<RepoIpReferDo>() {
			@Override
			public List<RepoIpReferDo> fetchBatchTuplesAscByPrimaryId(int beginPrimaryId) throws Exception {
				return repoIpReferDao.fetchBatchTuples(beginPrimaryId);
			}
			@Override
			public int processOneIpSampleAndReturnCurrentProcessPrimaryId(RepoIpReferDo ird, List<RepoIpReferDo> fetchedResultList,int currIndex) {
				int primaryId = ird.getIpReferId();
				try {
					String ipSample = StringUtils.trimToNull(ird.getIpStr());
					ProfileCity profile = locationIpApiAliyun.getResponseGently(ipSample, 10);
					if (profile!=null && !profile.isEmpty()) {
						RepoIpRangeDo ipRangeDo = repoIpRangeDao.fetchMinimurnContainedTuple(UtilIpConverter.toLongValue(ird.getIpStr()));
						RepoIpReferDo updatedDo = UtilBeanToDoConverter.convert2IpReferAsPossible(profile);
						updatedDo.setIpStr(ird.getIpStr());
						updatedDo.setIpReferId(ird.getIpReferId());
						updatedDo.setIpRangeId(ipRangeDo.getIpRangeId());
						repoIpReferDao.updateByPrimaryKeySelective(updatedDo);
					}
				} catch (Exception e) {
					logger.info(e.getMessage(), e);
				}
		    	return primaryId;
			}
    	}.doProcess();
    	logger.info("RoutineRepoIpReferRefreshing-doRoutine finished......");
    }

	public void setLocationIpApiAliyun(LocationIpApiAliyun locationIpApiAliyun) {
		this.locationIpApiAliyun = locationIpApiAliyun;
	}
	public void setRepoIpReferDao(RepoIpReferDao repoIpReferDao) {
		this.repoIpReferDao = repoIpReferDao;
	}
	public void setRepoIpRangeDao(RepoIpRangeDao repoIpRangeDao) {
		this.repoIpRangeDao = repoIpRangeDao;
	}
}
