package com.yunrang.location.integration.support.file.history.data_201306;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunrang.location.common.bean.ProfileIpRange;
import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.common.util.UtilIpConverter;
import com.yunrang.location.datamgr.mysql.dao.RepoIpRangeDao;
import com.yunrang.location.datamgr.mysql.util.UtilBeanToDoConverter;
import com.yunrang.location.integration.support.api.ip.LocationIpApiSinaIPRange;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;


/**
ID,start_ip,end_ip,province,city,isp,start_ip1,end_ip1
1,3708713472,3708715007,河南省,信阳市,联通,221.14.122.0,221.14.127.255
2,3708649472,3708813311,河南省,,联通,221.13.128.0,221.15.255.255
3,3720390656,3720391679,河北省,邢台市,联通,221.192.168.0,221.192.171.255
4,1038992128,1038992383,黑龙江省,齐齐哈尔市,铁通,61.237.195.0,61.237.195.255
5,3683853312,3683853567,山西省,晋城市,电信,219.147.36.0,219.147.36.255
6,3395483392,3395483903,西藏,那曲地区,电信,202.98.247.0,202.98.248.255
7,3658842112,3658874879,内蒙古,,联通,218.21.128.0,218.21.255.255
8,1970911232,1970913279,北京市,,联通,117.121.184.0,117.121.191.255
9,1866106880,1866107903,广西,柳州市,移通,111.58.136.0,111.58.139.255 
 */
public class PreprocessCityIpTxt implements HistoryFilesConfig.Processor {
    private static final Logger Log = LoggerFactory.getLogger(PreprocessCityIpTxt.class);
    
    private LocationIpApiSinaIPRange aPISinaIPRangeLocation;
    private RepoIpRangeDao repoIpRangeDao;
    
	public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doSingleThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/city_ip.txt", new ContextFileProcess.LineProcessor(){
            private Long beginPoint = 7500L;
            private Long count = 0L;
            public void processLine(String line) {
                count++;
                try {
                    if (count >= beginPoint) {
                        String[] fields = line.split(",");
                        String ipStrStartTmp = fields[6];
                        String ipStrEndTmp = fields[7];
                        Long ipStrStartTmpNum = UtilIpConverter.toLongValue(ipStrStartTmp);
                        Long ipStrEndTmpNum = UtilIpConverter.toLongValue(ipStrEndTmp);
                        if ((count % 100)==0) Log.info(count+" records have been processed...");
                        for (String ipSample : genSampleIPSet(ipStrStartTmpNum, ipStrEndTmpNum)) {
                            ProfileIpRange profile = aPISinaIPRangeLocation.getResponse(ipSample);
                            insertToRange(profile);
                            Thread.sleep(100);
                        }
                    }
                } catch (Exception e) {
                    Log.info(line,e);
                }
            }
        });
    }
    
    private Set<String> genSampleIPSet(Long ipStrStartTmpNum, Long ipStrEndTmpNum) {
        Integer offsetNum = 4;
        Set<String> ipSet = new HashSet<String>();
        Long offSet = (ipStrEndTmpNum - ipStrStartTmpNum) / offsetNum;
        for (int i=0; i < offsetNum; i++) {
            Long ipSampleNum = ipStrStartTmpNum + offSet*i;
            if (ipSampleNum <= ipStrEndTmpNum) {
                String ipSample = UtilIpConverter.toStringValue(ipSampleNum);
                ipSet.add(ipSample);
            }
        }
        return ipSet;
    }
    
    private void insertToRange(ProfileIpRange profile) throws Exception {
        Long ipStrStartNum = profile.getIpStrStartNum();
        Long ipStrEndNum = profile.getIpStrEndNum();
        if (ipStrStartNum > 0 && ipStrEndNum > 0) {
            if (repoIpRangeDao.fetchDupeTuplesCount(profile.getIpStrStartNum(), profile.getIpStrEndNum()) == 0L) {
                repoIpRangeDao.insertOneTuple(UtilBeanToDoConverter.convert2IpRangeDo(profile));
            } 
        }
    }
    
    public void setaPISinaIPRangeLocation(
			LocationIpApiSinaIPRange aPISinaIPRangeLocation) {
		this.aPISinaIPRangeLocation = aPISinaIPRangeLocation;
	}
	public void setRepoIpRangeDao(RepoIpRangeDao repoIpRangeDao) {
		this.repoIpRangeDao = repoIpRangeDao;
	}
}
