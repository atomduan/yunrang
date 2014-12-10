package com.yunrang.location.integration.support.file.history.data_201306;

import java.sql.SQLException;

import com.yunrang.location.common.bean.ProfilePoi;
import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.common.context.ContextPoiQuery;
import com.yunrang.location.datamgr.mysql.dao.RepoIpReferDao;
import com.yunrang.location.datamgr.mysql.util.UtilBeanToDoConverter;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;


/**
211.138.125.213 330700
211.138.125.212 330700
125.88.122.103 440981
112.97.30.1 440300
27.24.141.21 421200
27.24.141.24 421200
27.24.141.13 421200
218.202.4.135 530100
221.136.152.196 330200
218.202.4.137 530100
 */

public class PreprocessIpValidateAds20130403Txt implements HistoryFilesConfig.Processor {
    private final String dupKeyPatterRegex = "Duplicate entry";
    
    private ContextPoiQuery poiQueryingContext;
    private RepoIpReferDao repoIpReferDao;
    
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/ip_validate_ads_20130403.txt", new ContextFileProcess.LineProcessor(){
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
