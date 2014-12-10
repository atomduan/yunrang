package com.yunrang.location.service.report;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunrang.location.common.bean.ProfileCity;
import com.yunrang.location.common.bean.ProfileIpRange;
import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.common.context.ContextPoiQuery;
import com.yunrang.location.integration.support.api.ip.LocationIpApi138;
import com.yunrang.location.service.support.ip.LocationSinaIpRange;

public class ReportCnzzIpQuality {
	private static final Logger logger = LoggerFactory.getLogger(ReportCnzzIpQuality.class);
	
	private String cnzzDataRoot = "/var/yr/ads_data/cnzz";
	private Map<String, ProfileCity> ipRecordMap = new HashMap<String, ProfileCity>();
	
	private ContextPoiQuery contextPoiQuery;
	private LocationSinaIpRange locationSinaIpRange;
	private LocationIpApi138 locationIpApi138;
	
	private int totalCnzzRecord = 0;
	private int totalCnzzHit = 0;
	private int totalSinaHit = 0;
	private int totalIp138Hit = 0;
	
	private int sinaFaultCount = 0;
	private int cnzzFaultCount = 0;
	private int bothFaultCount = 0;
	
	private int cnzzAndSinaSameCount = 0;
	private int cnzzAndSinaDiffCount = 0;
	private int cnzzAndSinaSameProvinceCount = 0; 
	
	private int cnzzAndSinaDiffProvinceCount = 0;
	private int cnzzAndSinaDiffCnzzNull = 0;
	private int cnzzAndSinaDiffCnzzMoreDetailCount = 0;
	private int cnzzAndSinaDiffSinaMoreDetailCount = 0;
	
	private int cnzzAndSinaSameProvinceDiffCityCount = 0;
	
	private void loadTotalIpRecords(Map<String, ProfileCity> ipRecordMap) {
		for (int i=1; i<31; i++) {
			String filePath = cnzzDataRoot+"/main ("+i+").php";
			loadOneFileIpRecords(filePath, ipRecordMap);
		}
	}
	
	
	public void parseResultIpRecords() {
		Map<String, ProfileCity> ipRecordMap = new HashMap<String, ProfileCity>();
		for (int i=1; i<18; i++) {
			String filePath = cnzzDataRoot+"/main ("+i+").php";
			loadOneFileIpRecords(filePath, ipRecordMap);
		}
		
		int totalIpCount = ipRecordMap.size();
		final Set<String> resultSet= new HashSet<String>();
		ContextFileProcess fileProcessContext = new ContextFileProcess();
		fileProcessContext.doSingleThreadProcess(cnzzDataRoot+"/result.log", new ContextFileProcess.LineProcessor(){
			public void processLine(String line) throws Exception {
				String indicator = "[main] INFO  com.yunrang.location.computation.report.ReportCnzzIpQuality  - ";
				String rawResult = line.substring(line.indexOf(indicator)).replace(indicator, "");
				resultSet.add(rawResult);
			}
			public void cleanup() {
				System.out.println(resultSet.size());
			}
		});
		System.out.println("totalIpCount:"+totalIpCount);
		//###################################################
		int totalValideIpCount = totalIpCount;
		int totalFaultCount = resultSet.size();
		int faultCnzzCount = 0;
		int detailFaultCnzzCount = 0;
		int faultSinaCount = 0;
		int detailFaultSinaCount = 0;
		int faultBothCount = 0;
		for (String rawResult : resultSet) {
			if (rawResult.contains("Fault cnzz detected")) {
				faultCnzzCount++;
				if (isMoreDetailedFault(rawResult)) {
					detailFaultCnzzCount++;
				}
			}
			if (rawResult.contains("Fault sina detected")) {
				faultSinaCount++;
				if (isMoreDetailedFault(rawResult)) {
					detailFaultSinaCount++;
				} 
			}
			if (rawResult.contains("Both Fault detected")) {
				System.out.println(rawResult);
				faultBothCount++;
			}
			
		}
		System.out.println("totalValideIpCount:"+totalValideIpCount);
		System.out.println("totalFaultCount:"+totalFaultCount);
		System.out.println("faultCnzzCount:"+faultCnzzCount);
		System.out.println("detailFaultCnzzCount:"+detailFaultCnzzCount);
		System.out.println("faultSinaCount:"+faultSinaCount);
		System.out.println("detailFaultSinaCount:"+detailFaultSinaCount);
		System.out.println("faultBothCount:"+faultBothCount);
	}
	
	private boolean isMoreDetailedFault(String rawResult) {
		
		String[] base = this.getRrightBaseStringArray(rawResult);
		int baseLangth = 0;
		String baseProvinceCode = contextPoiQuery.getProvinceCode(StringUtils.trimToNull(base[2].replace("null", "")));
		if (baseProvinceCode != null) baseLangth++;
		String baseCityCode = contextPoiQuery.getCityCode(StringUtils.trimToNull(base[1].replace("null", "")));
		if (baseCityCode != null) baseLangth++;
		String baseDistrictCode = contextPoiQuery.getDistrictCode(StringUtils.trimToNull(base[0].replace("null", "")), baseCityCode);
		if (baseDistrictCode != null) baseLangth++;
		String[] target = this.getTargetStringArray(rawResult);
		int targetLangth = 0;
		String targetProvinceCode = contextPoiQuery.getProvinceCode(StringUtils.trimToNull(target[2].replace("null", "")));
		if (targetProvinceCode != null) targetLangth++;
		String targetCityCode = contextPoiQuery.getCityCode(StringUtils.trimToNull(target[1].replace("null", "")));
		if (targetCityCode != null) targetLangth++;
		String targetDistrictCode = contextPoiQuery.getDistrictCode(StringUtils.trimToNull(target[0].replace("null", "")), targetCityCode);
		if (targetDistrictCode != null) targetLangth++;
		if (baseLangth != targetLangth) {
			return true;
		}
		return false;
	}
	
	private String[] getTargetStringArray(String rawResult) {
		String baseStr = rawResult.substring(rawResult.indexOf("is[")).replace("is[", "");
		baseStr = baseStr.substring(0, baseStr.indexOf("]"));
		return baseStr.trim().split("/");
	}
	
	private String[] getRrightBaseStringArray(String rawResult) {
		String baseStr = rawResult.substring(rawResult.indexOf("is [")).replace("is [", "");
		baseStr = baseStr.substring(0, baseStr.indexOf("]"));
		return baseStr.trim().split("/");
	}
	
	private void loadOneFileIpRecords(final String filePath, final Map<String, ProfileCity> ipRecordMap) {
		ContextFileProcess fileProcessContext = new ContextFileProcess();
		fileProcessContext.doSingleThreadProcess(filePath, "GBK", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) throws Exception {
            	String[] params = StringUtils.trimToEmpty(line.replace("\t", "")).split(",");
            	if (params.length == 14) {
            		String ipStr = params[4];
            		if (ipStr.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            			String[] addrParams = params[5].split("-");
                		String provinceName = addrParams[0];
                		String provinceCode = contextPoiQuery.getProvinceCode(provinceName);
                		String cityName = addrParams.length==2 ? addrParams[1] : null;
                		if (contextPoiQuery.isDCCityName(provinceName)) {
                			cityName = provinceName;
                		}
                		String cityCode = contextPoiQuery.getCityCode(cityName);
                		ProfileCity profileCity = new ProfileCity();
                		profileCity.setProvinceName(provinceName);
                		profileCity.setProvinceCode(provinceCode);
                		profileCity.setCityName(cityName);
                		profileCity.setCityCode(cityCode);
                		ipRecordMap.put(ipStr, profileCity);
            		}
            	}
            }
        });
	}
	
	
	public void qualityReport() {
		loadTotalIpRecords(ipRecordMap);
		this.totalCnzzRecord = ipRecordMap.size();
		for (String ipStr : ipRecordMap.keySet()) {
			ProfileCity cnzz = ipRecordMap.get(ipStr);
			if (!cnzz.isEmpty()) {
				this.totalCnzzHit++;
				ProfileCity ip138 = consultFromIp138(ipStr);
				if (!ip138.isEmpty()) {
					this.totalIp138Hit++;
					ProfileCity sina = consultFromSinaIpRangeLocal(ipStr);
					if (!sina.isEmpty()) {
						this.totalSinaHit++;
						doQualityReport(cnzz, ip138, sina, ipStr);
					}
				}
			}
		}
		logger.info("cnzz 条目总数："+this.totalCnzzRecord);
		logger.info("\t cnzz 命中总数："+this.totalCnzzHit);
		logger.info("\t\t ip138 命中总数："+this.totalIp138Hit);
		logger.info("\t\t\t sina 命中总数："+this.totalSinaHit);
		logger.info("\t\t\t\t sina错误总数："+this.sinaFaultCount);
		logger.info("\t\t\t\t cnzz错误总数："+this.cnzzFaultCount);
		logger.info("\t\t\t\t 两者均错误总数："+this.bothFaultCount);
	}
	
	public void cnzzAndSinaDiffReport() {
		loadTotalIpRecords(ipRecordMap);
		this.totalCnzzRecord = ipRecordMap.size();
		for (String ipStr : ipRecordMap.keySet()) {
			ProfileCity cnzz = ipRecordMap.get(ipStr);
			if (!cnzz.isEmpty()) {
				this.totalCnzzHit++;
				ProfileCity sina = consultFromSinaIpRangeLocal(ipStr);
				if (!sina.isEmpty()) {
					this.totalSinaHit++;
					if (cnzz.equals(sina)) {
						this.cnzzAndSinaSameCount++;
					} else {
						this.cnzzAndSinaDiffCount++;
						if (!StringUtils.isBlank(cnzz.getProvinceCode()) && !StringUtils.isBlank(sina.getProvinceCode())) {
							if (cnzz.getProvinceCode().equals(sina.getProvinceCode())) {
								if (StringUtils.isBlank(cnzz.getCityCode()) && !StringUtils.isBlank(sina.getCityCode())) {
									this.cnzzAndSinaDiffSinaMoreDetailCount++;
								} else if (StringUtils.isBlank(sina.getCityCode()) && !StringUtils.isBlank(cnzz.getCityCode())) {
									this.cnzzAndSinaDiffCnzzMoreDetailCount++;
								} else {
									printDiffDetail(cnzz, sina, ipStr);
									this.cnzzAndSinaSameProvinceDiffCityCount++;
								}
							} else {
								this.cnzzAndSinaDiffProvinceCount++;
							}
						} else {
							this.cnzzAndSinaDiffCnzzNull++;
						}
					}
					//get the same province count
					if (!StringUtils.isBlank(cnzz.getProvinceCode()) && !StringUtils.isBlank(sina.getProvinceCode())) {
						if (cnzz.getProvinceCode().equals(sina.getProvinceCode())) {
							this.cnzzAndSinaSameProvinceCount++;
						}
					}
				}
			}
		}
		logger.info("cnzz 条目总数："+this.totalCnzzRecord);
		logger.info("\t cnzz 命中总数："+this.totalCnzzHit);
		logger.info("\t\t\t ‘本地定位’ 命中总数："+this.totalSinaHit);
		
		logger.info("\t\t\t\t cnzz和‘本地定位’结果省份相同数："+this.cnzzAndSinaSameProvinceCount+"\n");
		logger.info("\t\t\t\t cnzz和‘本地定位’结果城市相同数："+this.cnzzAndSinaSameCount);
		logger.info("\t\t\t\t cnzz和‘本地定位’结果城市不同数："+this.cnzzAndSinaDiffCount);
		
		logger.info("\t\t\t\t\t 结果不同并且省份就不同："+this.cnzzAndSinaDiffProvinceCount);
		logger.info("\t\t\t\t\t 结果不同并且CNZZ空数据："+this.cnzzAndSinaDiffCnzzNull);
		logger.info("\t\t\t\t\t\t 结果不同但省份相同-‘本地定位’有城市-cnzz无城市："+this.cnzzAndSinaDiffSinaMoreDetailCount);
		logger.info("\t\t\t\t\t\t 结果不同但省份相同-cnzz有城市-‘本地定位’无城市："+this.cnzzAndSinaDiffCnzzMoreDetailCount);
		logger.info("\t\t\t\t\t\t 结果不同但省份相同-都有城市-但为不同城市："+this.cnzzAndSinaSameProvinceDiffCityCount);
	}

	private void printDiffDetail(ProfileCity cnzz, ProfileCity sina, String ipStr) {
		String message = "ipStr is ["+ipStr+"];" +
				"cnzz is ["+cnzz.getCityName()+"/"+cnzz.getCityCode()+"/"+cnzz.getProvinceName()+"/"+cnzz.getProvinceCode()+"/"+"];" +
				"local is ["+sina.getCityName()+"/"+sina.getCityCode()+"/"+sina.getProvinceName()+"/"+sina.getProvinceCode()+"/"+"]";
		System.out.println(message);
	}
	
	private void doQualityReport(ProfileCity cnzz, ProfileCity ip138, ProfileCity sina, String ipStr) {
		if (cnzz.equals(ip138)) {
			if (!cnzz.equals(sina)) {
				logger.info("Fault sina detected: ipstr["+ipStr+"]; " +
						"ip138 and cnzz is ["+ip138.getDistrictName()+"/"+ip138.getCityName()+"/"+ip138.getProvinceName()+"], " +
						"but sina is["+sina.getDistrictName()+"/"+sina.getCityName()+"/"+sina.getProvinceName()+"]");
				this.sinaFaultCount++;
			}
		} else 
		if (sina.equals(ip138)) {
			if (!sina.equals(cnzz)) {
				logger.info("Fault cnzz detected: ipstr["+ipStr+"]; " +
						"ip138 and sina is ["+ip138.getDistrictName()+"/"+ip138.getCityName()+"/"+ip138.getProvinceName()+"], " +
						"but cnzz is["+cnzz.getDistrictName()+"/"+cnzz.getCityName()+"/"+cnzz.getProvinceName()+"]");
				this.cnzzFaultCount++;
			}
		} else {
			logger.info("Both Fault detected: ipstr["+ipStr+"]; " +
					"cnzz is ["+cnzz.getDistrictName()+"/"+cnzz.getCityName()+"/"+cnzz.getProvinceName()+"], " +
					"sina is ["+sina.getDistrictName()+"/"+sina.getCityName()+"/"+sina.getProvinceName()+"], " +
					"ip138 is["+ip138.getDistrictName()+"/"+ip138.getCityName()+"/"+ip138.getProvinceName()+"]");
			this.bothFaultCount++;
		} 
	}
	
	private ProfileCity consultFromSinaIpRangeLocal(String ipStr) {
		ProfileCity profileCity = new ProfileCity();
		ProfileIpRange profileIpRange = this.locationSinaIpRange.getIpRangeInfoForThisIp(ipStr);
		if (profileIpRange != null) {
			profileCity.setCityCode(profileIpRange.getCityCode());
			profileCity.setCityName(profileIpRange.getCityName());
			profileCity.setDistrictCode(profileIpRange.getDistrictCode());
			profileCity.setDistrictName(profileIpRange.getDistrictName());
			profileCity.setProvinceCode(profileIpRange.getProvinceCode());
			profileCity.setProvinceName(profileIpRange.getProvinceName());
			profileCity.setOptionalInfo(profileIpRange.getDescription());
		}
		return profileCity;
	}
	
	private Random randomSec = new Random();
	private int failCount = 0;
	private ProfileCity consultFromIp138(String ipStr) {
		try {
			if (this.failCount > 15) {
				this.failCount = 0;
				logger.info("failcount exceed 15, sleep 3600 sec.....");
				Thread.sleep(60*60*1000);
			} else {
				int sec = randomSec.nextInt(10)+4;
				Thread.sleep(sec*1000);
			}
			ProfileCity profileCity = locationIpApi138.getResponse(ipStr);
			if (profileCity.isEmpty()) {
				this.failCount++;
			}
			return profileCity;
		} catch (Exception e) {
			this.failCount++;
			return new ProfileCity();
		}
	}


	public void setCnzzDataRoot(String cnzzDataRoot) {
		this.cnzzDataRoot = cnzzDataRoot;
	}
	public void setIpRecordMap(Map<String, ProfileCity> ipRecordMap) {
		this.ipRecordMap = ipRecordMap;
	}
	public void setContextPoiQuery(ContextPoiQuery contextPoiQuery) {
		this.contextPoiQuery = contextPoiQuery;
	}
	public void setLocationSinaIpRange(LocationSinaIpRange locationSinaIpRange) {
		this.locationSinaIpRange = locationSinaIpRange;
	}
	public void setLocationIpApi138(LocationIpApi138 locationIpApi138) {
		this.locationIpApi138 = locationIpApi138;
	}
	public void setTotalCnzzRecord(int totalCnzzRecord) {
		this.totalCnzzRecord = totalCnzzRecord;
	}
	public void setTotalCnzzHit(int totalCnzzHit) {
		this.totalCnzzHit = totalCnzzHit;
	}
	public void setTotalSinaHit(int totalSinaHit) {
		this.totalSinaHit = totalSinaHit;
	}
	public void setTotalIp138Hit(int totalIp138Hit) {
		this.totalIp138Hit = totalIp138Hit;
	}
	public void setSinaFaultCount(int sinaFaultCount) {
		this.sinaFaultCount = sinaFaultCount;
	}
	public void setCnzzFaultCount(int cnzzFaultCount) {
		this.cnzzFaultCount = cnzzFaultCount;
	}
	public void setBothFaultCount(int bothFaultCount) {
		this.bothFaultCount = bothFaultCount;
	}
	public void setCnzzAndSinaSameCount(int cnzzAndSinaSameCount) {
		this.cnzzAndSinaSameCount = cnzzAndSinaSameCount;
	}
	public void setCnzzAndSinaDiffCount(int cnzzAndSinaDiffCount) {
		this.cnzzAndSinaDiffCount = cnzzAndSinaDiffCount;
	}
	public void setCnzzAndSinaSameProvinceCount(int cnzzAndSinaSameProvinceCount) {
		this.cnzzAndSinaSameProvinceCount = cnzzAndSinaSameProvinceCount;
	}
	public void setCnzzAndSinaDiffProvinceCount(int cnzzAndSinaDiffProvinceCount) {
		this.cnzzAndSinaDiffProvinceCount = cnzzAndSinaDiffProvinceCount;
	}
	public void setCnzzAndSinaDiffCnzzNull(int cnzzAndSinaDiffCnzzNull) {
		this.cnzzAndSinaDiffCnzzNull = cnzzAndSinaDiffCnzzNull;
	}
	public void setCnzzAndSinaDiffCnzzMoreDetailCount(int cnzzAndSinaDiffCnzzMoreDetailCount) {
		this.cnzzAndSinaDiffCnzzMoreDetailCount = cnzzAndSinaDiffCnzzMoreDetailCount;
	}
	public void setCnzzAndSinaDiffSinaMoreDetailCount(int cnzzAndSinaDiffSinaMoreDetailCount) {
		this.cnzzAndSinaDiffSinaMoreDetailCount = cnzzAndSinaDiffSinaMoreDetailCount;
	}
	public void setCnzzAndSinaSameProvinceDiffCityCount(int cnzzAndSinaSameProvinceDiffCityCount) {
		this.cnzzAndSinaSameProvinceDiffCityCount = cnzzAndSinaSameProvinceDiffCityCount;
	}
	public void setRandomSec(Random randomSec) {
		this.randomSec = randomSec;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
}
