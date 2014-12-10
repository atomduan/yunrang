package com.yunrang.location.service.support.ip;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.yunrang.location.common.bean.ProfileIpRange;
import com.yunrang.location.common.util.UtilIpConverter;
import com.yunrang.location.service.util.UtilLocationResult;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

public class LocationSinaIpRange implements InitializingBean {
    static final Logger Log = LoggerFactory.getLogger(LocationSinaIpRange.class);
    private final String fileName = "data_location/repo_ip_range.csv";
    
    private TreeMap<Long, ProfileIpRange> rangeMap;
    private List<ProfileIpRange> ipInfos;

	@Override
	public void afterPropertiesSet() throws Exception {
		initialization();
	}
	
    private void initialization() throws Exception {
        ipInfos = readFromFile(fileName);
        rangeMap = new TreeMap<Long, ProfileIpRange>(new Comparator<Long>() {
            public int compare(Long o1, Long o2) {
                return o1.compareTo(o2);
            }
        });
        for (ProfileIpRange ipInfo : ipInfos) {
            rangeMap.put(ipInfo.getIpStrStartNum(), ipInfo);
        }
    }
    
    /**
     * "11","1","60.55.0.0","1010237440","60.55.255.255","1010302975","浙江","33","宁波","3302",,NULL,"联通",,,"0000-00-00 00:00:00"
     */
    private List<ProfileIpRange> readFromFile(String filename) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        CSVReader reader = new CSVReader(new InputStreamReader(is), ',', '"', 1);
        ColumnPositionMappingStrategy<ProfileIpRange> strat = new ColumnPositionMappingStrategy<ProfileIpRange>();
        strat.setType(ProfileIpRange.class);
        String[] columns = new String[] {
                "ipRangeId",
                "ret",
                "ipStrStart",
                "ipStrStartNum",
                "ipStrEnd",
                "ipStrEndNum",
                "provinceName",
                "provinceCode",
                "cityName",
                "cityCode",
                "districName",
                "districCode",
                "isp",
                "type",
                "description",
                "updateTime"
        };
        strat.setColumnMapping(columns);
        CsvToBean<ProfileIpRange> csv = new CsvToBean<ProfileIpRange>();
        List<ProfileIpRange> list = csv.parse(strat, reader);
        return list;
    }
    
    public ProfileIpRange getIpRangeInfoForThisIp(String ip) {
    	return this.getEntry(UtilIpConverter.toLongValue(ip));
    }
    
    public TreeMap<Long, ProfileIpRange> getIpRangeMap() {
    	return this.rangeMap;
    }
    
    public List<String> getProvinceAndCity(String ip) {
        ProfileIpRange ipInfo = getEntry(UtilIpConverter.toLongValue(ip));
        String description = ipInfo.getDescription();
        String provinceName = normalizeLocationName(ipInfo.getProvinceName()); 
        String cityName = normalizeLocationName(ipInfo.getCityName());
        String districtName = normalizeLocationName(ipInfo.getDistrictName());
        // 全省出口IP，无法准确到市，退化为省处理
        if (description!=null && description.contains("出口") && description.contains("全省")) {
            cityName = null;
            districtName = null;
        }
        return UtilLocationResult.getProvinceAndCityListResult(provinceName, cityName, districtName);
    }
    
    private String normalizeLocationName(String name) {
    	if (name != null) {
    		return StringUtils.trimToNull(name.replaceAll("NULL", ""));
    	} else {
    		return name;
    	}
    }
    
    private ProfileIpRange getEntry(long encodedIP) {
        ProfileIpRange ipRangeInfo = null;
        Entry<Long, ProfileIpRange> mapEntry = rangeMap.floorEntry(encodedIP);
        if (mapEntry != null) {
            ProfileIpRange ipRangeInfoTmp = mapEntry.getValue();
            if (ipRangeInfoTmp.getIpStrStartNum() <= encodedIP 
                    && encodedIP <= ipRangeInfoTmp.getIpStrEndNum()) {
                ipRangeInfo = ipRangeInfoTmp;
            }
        }
        return ipRangeInfo;
    }
}
