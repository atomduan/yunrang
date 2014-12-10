package com.yunrang.location.integration.support.file.history.data_201306;

import java.sql.SQLException;

import com.yunrang.location.common.bean.ProfilePoi;
import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.common.context.ContextPoiQuery;
import com.yunrang.location.datamgr.mysql.dao.RepoIpReferDao;
import com.yunrang.location.datamgr.mysql.util.UtilBeanToDoConverter;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;


/**
61.50.248.10 110112
61.148.244.99 110000
61.148.244.87 110000
61.148.244.83 110000
61.148.244.78 110000
61.148.244.72 110000
61.148.244.64 110000
61.148.244.61 110000
61.148.244.6 110000
61.148.244.58 110000
 */
public class PreprocessIpValidateAdsBaseTxt implements HistoryFilesConfig.Processor {
    private final String dupKeyPatterRegex = "Duplicate entry";
    
    private ContextPoiQuery poiQueryingContext;
    private RepoIpReferDao repoIpReferDao;
    
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/ip_validate_ads_base.txt", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) throws Exception {
                ProfilePoi location = parseLine(line);
                try {
                    repoIpReferDao.insertOne(UtilBeanToDoConverter.convert2IpReferDo(location));
                } catch (SQLException e) {
                    if (!e.getMessage().trim().contains(dupKeyPatterRegex)) throw e;
                }
            }
        });
    }
    
    private ProfilePoi parseLine(String line) {
        ProfilePoi location = new ProfilePoi();
        String[] fields = line.split(" ");
        String ipStr = fields[0].trim();
        String code = fields[1].trim();
        String provinceCode = code.substring(0,2);
        String cityCode = code.substring(0,4);
        String districtCode = code.endsWith("00") ? null : code;
        String districtName = districtCode == null ? null : poiQueryingContext.getDistrictCanonicalName(districtCode);
        location.setIpStr(ipStr);
        location.setProvinceCode(provinceCode);
        location.setProvinceName(poiQueryingContext.getProvinceCanonicalName(provinceCode));
        location.setCityCode(cityCode);
        location.setCityName(poiQueryingContext.getCityCanonicalName(cityCode));
        location.setDistrictCode(districtCode);
        location.setDistrictName(districtName);
        return location;
    }

	public void setPoiQueryingContext(ContextPoiQuery poiQueryingContext) {
		this.poiQueryingContext = poiQueryingContext;
	}
	public void setRepoIpReferDao(RepoIpReferDao repoIpReferDao) {
		this.repoIpReferDao = repoIpReferDao;
	}
}
