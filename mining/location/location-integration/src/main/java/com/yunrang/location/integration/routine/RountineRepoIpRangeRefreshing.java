package com.yunrang.location.integration.routine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunrang.location.common.bean.ProfileIpRange;
import com.yunrang.location.common.context.ContextDataBaseBatchUpdate;
import com.yunrang.location.common.context.ContextPoiQuery;
import com.yunrang.location.common.util.UtilIpConverter;
import com.yunrang.location.datamgr.mysql.dao.RepoIpRangeDao;
import com.yunrang.location.datamgr.mysql.model.RepoIpRangeDo;
import com.yunrang.location.datamgr.mysql.util.UtilBeanToDoConverter;
import com.yunrang.location.integration.support.api.ip.LocationIpApiSinaIPRange;

public class RountineRepoIpRangeRefreshing {
    private static final Logger logger = LoggerFactory.getLogger(RountineRepoIpRangeRefreshing.class);
    
    private LocationIpApiSinaIPRange locationIpApiSinaIPRange;
    private ContextPoiQuery contextPoiQuery;
    private RepoIpRangeDao repoIpRangeDao;
    
	public void doRountine() throws Exception {
    	new ContextDataBaseBatchUpdate<RepoIpRangeDo>() {
			@Override
			public List<RepoIpRangeDo> fetchBatchTuplesAscByPrimaryId(int beginPrimaryId) throws Exception {
				return repoIpRangeDao.fetchBatchTuples(beginPrimaryId);
			}
			@Override
			public int processOneIpSampleAndReturnCurrentProcessPrimaryId(RepoIpRangeDo ird, List<RepoIpRangeDo> fetchedResultList, int currIndex) {
				try {
		    		RepoIpRangeDo lastProcessed = currIndex > 0 ? fetchedResultList.get(currIndex - 1) : ird;
		            for (String ipSample : genSampleIPSet(ird, lastProcessed)) {
		                processOneIpSample(ipSample);
		                Thread.sleep(100);
		            }
		            Thread.sleep(100);
		        } catch (Exception e) {
		            logger.warn(e.getMessage(), e);
		        }
		        return ird.getIpRangeId();
			}
		}.doProcess();
    }
    
    private void processOneIpSample(String ipSample) throws Exception {
        ProfileIpRange profile = locationIpApiSinaIPRange.getResponse(ipSample);
        refreshOrInsertToRange(profile);
    }
    
    private Set<String> genSampleIPSet(RepoIpRangeDo currentProcessed, RepoIpRangeDo lastProcessed) {
        Set<String> ipSet = new HashSet<String>();
        // split current range
        Long currentIpStrStartNum = UtilIpConverter.toLongValue(currentProcessed.getIpStrStart());
        Long currentIpStrEndNum = UtilIpConverter.toLongValue(currentProcessed.getIpStrEnd());
        fillRangedSampleIpSet(currentIpStrStartNum, currentIpStrEndNum, ipSet);
        // detected the marginal
        fillRangedSampleIpSetMarginal(currentIpStrStartNum, currentIpStrEndNum, ipSet);
        // try to split space between to ranges
        try {
            Long lastIpStrStartNum = UtilIpConverter.toLongValue(lastProcessed.getIpStrStart());
            Long lastIpStrEndNum = UtilIpConverter.toLongValue(lastProcessed.getIpStrEnd());
            
            if (lastIpStrEndNum < currentIpStrStartNum) {
                fillRangedSampleIpSet(lastIpStrEndNum, currentIpStrStartNum, ipSet);
            } 
            if (currentIpStrEndNum < lastIpStrStartNum) {
                fillRangedSampleIpSet(currentIpStrEndNum, lastIpStrStartNum, ipSet);
            }
        } catch (Exception ignore) {}
        return ipSet;
    }
    
    private void fillRangedSampleIpSetMarginal(Long currentIpStrStartNum, Long currentIpStrEndNum, Set<String> ipSet) {
        Long offsetNum = 43L;
        Integer retryNum = 7;
        for (int i=1; i<retryNum; i++) {
            offsetNum = (long) (offsetNum * 1.618);
            ipSet.add(UtilIpConverter.toStringValue(currentIpStrEndNum+offsetNum));
            ipSet.add(UtilIpConverter.toStringValue(currentIpStrStartNum-offsetNum));
        }
    }
    
    private void fillRangedSampleIpSet(Long bIp, Long eIp, Set<String> ipSet) {
        Long offsetNum = 4L;
        Long offSet = (eIp - bIp) / offsetNum;
        for (int i=0; i < offsetNum; i++) {
            Long ipSampleNum = bIp + offSet*i;
            if (ipSampleNum <= eIp) {
                String ipSample = UtilIpConverter.toStringValue(ipSampleNum);
                ipSet.add(ipSample);
            }
        }
    }
    
    private void refreshOrInsertToRange(ProfileIpRange profile) throws Exception {
        Long ipStrStartNum = profile.getIpStrStartNum();
        Long ipStrEndNum = profile.getIpStrEndNum();
        if (this.contextPoiQuery.isChineseProfileIpRange(profile)) {
        	profile.setCountryName("中国");
            if (ipStrStartNum > 0 && ipStrEndNum > 0) {
                if (repoIpRangeDao.fetchDupeTuplesCount(profile.getIpStrStartNum(), profile.getIpStrEndNum()) == 0L) {
                    repoIpRangeDao.insertOneTuple(UtilBeanToDoConverter.convert2IpRangeDo(profile));
                } else {
                    repoIpRangeDao.refreshTuple(UtilBeanToDoConverter.convert2IpRangeDo(profile));
                }
            }
        }
    }
    
    public void setLocationIpApiSinaIPRange(LocationIpApiSinaIPRange locationIpApiSinaIPRange) {
		this.locationIpApiSinaIPRange = locationIpApiSinaIPRange;
	}
	public void setContextPoiQuery(ContextPoiQuery contextPoiQuery) {
		this.contextPoiQuery = contextPoiQuery;
	}
	public void setRepoIpRangeDao(RepoIpRangeDao repoIpRangeDao) {
		this.repoIpRangeDao = repoIpRangeDao;
	}
}
