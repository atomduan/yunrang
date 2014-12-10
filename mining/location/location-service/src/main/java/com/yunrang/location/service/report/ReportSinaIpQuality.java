package com.yunrang.location.service.report;

import java.util.Map;

import com.yunrang.location.common.bean.ProfileCity;
import com.yunrang.location.common.bean.ProfileIpRange;
import com.yunrang.location.common.util.UtilIpConverter;
import com.yunrang.location.integration.support.api.ip.LocationIpApiAliyun;
import com.yunrang.location.service.support.ip.LocationSinaIpRange;

public class ReportSinaIpQuality {
	private LocationSinaIpRange locationSinaIpRange;
	private LocationIpApiAliyun locationIpApiAliyun;
	
	public void evaluate() {
		System.out.println("#############################  evaluate begin  #####################################");
		Integer remoteNotFound = 0;
		Integer totalCount = 0;
		Integer sameCityLocation = 0;
		Integer sameProvinceLocation = 0;
		Integer remoteLocationHit = 0;
		
		Map<Long, ProfileIpRange> ipRangeMap = locationSinaIpRange.getIpRangeMap();
		for (Long key : ipRangeMap.keySet()) {
			totalCount++;
			try {
				ProfileIpRange sinaProfileIpRange = ipRangeMap.get(key);
				long ipStrStartNum = sinaProfileIpRange.getIpStrStartNum();
				long ipStrEndNum = sinaProfileIpRange.getIpStrEndNum();
				String samleIpStr = UtilIpConverter.toStringValue((ipStrStartNum + ipStrEndNum)/2);
				Thread.sleep(3000);
				ProfileCity remoteProfileCity = locationIpApiAliyun.getResponse(samleIpStr);
				if (!remoteProfileCity.isEmpty()) {
					remoteLocationHit++;
					String remoteApiCityCode = remoteProfileCity.getCityCode();
					String sinaCityCode = sinaProfileIpRange.getCityCode();
					String remoteApiProvinceCode = remoteProfileCity.getProvinceCode();
					String sinaProvinceCode = sinaProfileIpRange.getProvinceCode();
					if (remoteApiCityCode != null && sinaCityCode != null) {
						if (remoteApiCityCode.equals(sinaCityCode)) {
							sameCityLocation++;
						}
					}
					if (remoteApiProvinceCode != null && sinaProvinceCode != null) {
						if (remoteApiProvinceCode.equals(sinaProvinceCode)) {
							sameProvinceLocation++;
						}
					}
				} else {
					remoteNotFound++;
				}
			} catch (Exception ignore){}
			if (totalCount%3 == 0) {
				System.out.println(totalCount+" count has been processed.....");
				System.out.println("#############################  统计结果  #####################################");
				System.out.println("totalCount	:	"+totalCount);
				System.out.println("sameCityLocation	:	"+sameCityLocation);
				System.out.println("sameProvinceLocation	:	"+sameProvinceLocation );
				System.out.println("remoteLocationHit	:	"+remoteLocationHit);
				System.out.println("remoteNotFound	:	"+remoteNotFound);
			}
		}
		System.out.println("#############################   evaluate finished  #####################################");
	}

	public void setLocationSinaIpRange(LocationSinaIpRange locationSinaIpRange) {
		this.locationSinaIpRange = locationSinaIpRange;
	}
	public void setLocationIpApiAliyun(LocationIpApiAliyun locationIpApiAliyun) {
		this.locationIpApiAliyun = locationIpApiAliyun;
	}
}