package com.yunrang.location.service.support.cell;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.yunrang.location.common.bean.ProfileCity;
import com.yunrang.location.service.support.geo.LocationGeoLight;
import com.yunrang.location.service.util.UtilLocationResult;

public class LocationCellLight implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(LocationCellLight.class);
    
    private final String JIZHAN_GEOLOCATION_MAP_FILE = "data_location/cell_light.txt";
    private final String SEPARATOR = " ";
    private final Map<String, Double[]> JIZHAN_GEOLOCATION_MAP  = new HashMap<String, Double[]>();
    private final String FIN_SEPARATOR = "#";
    private final String CHINA_JIZHAN_PATTERN_REGEX = "460\\d+";
    private final String CHINA_MCC_CODE = "460";
    
    private LocationGeoLight locationGeoLight;
    
	@Override
	public void afterPropertiesSet() throws Exception {
		initialization();
	}
	
    private void initialization() throws Exception {
        String filePath = JIZHAN_GEOLOCATION_MAP_FILE;
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            logger.info("LocationServiceJiZhanImpl loadToCacheMap begin.....");
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    String[] arr = line.split(SEPARATOR);
                    String mapKey = buildMapKey(arr[0],arr[1],arr[2],arr[3]);
                    Double lon = Double.parseDouble(arr[4]);
                    Double lat = Double.parseDouble(arr[5]);
                    // old value will be overwrite
                    JIZHAN_GEOLOCATION_MAP.put(mapKey, new Double[]{lon, lat});
                } else {
                    break;
                }
            }
            logger.info("LocationServiceJiZhanImpl loadToCacheMap finish.....");
        } finally {
            reader.close();
        }
    }
    
    public List<String> getProvinceAndCity(String NetworkOperator, String LAC, String CID) {
        try {
            if (NetworkOperator.matches(CHINA_JIZHAN_PATTERN_REGEX)) {
                String China_MCC = CHINA_MCC_CODE;
                String MNC = Integer.parseInt(NetworkOperator.substring(China_MCC.length(), NetworkOperator.length()))+"";
                Double[] geoLocation = getGeoLocation(China_MCC, MNC, LAC, CID);
                if (geoLocation != null) {
                    Double lon = geoLocation[0];
                    Double lat = geoLocation[1];
                    ProfileCity locationProfileCity = locationGeoLight.getLocationProfileCityBean(lon, lat);
                    if (locationProfileCity != null) {
                        String provinceName = locationProfileCity.getProvinceName();
                        String cityName = locationProfileCity.getCityName();
                        String districtName = locationProfileCity.getDistrictName();
                        return UtilLocationResult.getProvinceAndCityListResult(provinceName, cityName, districtName);
                    }
                }
            }
        } catch (NumberFormatException e) {}
        return null;
    }
    
    
    private String buildMapKey(String MCC, String MNC, String LAC, String CID) {
        StringBuilder sb = new StringBuilder();
        sb.append(MCC).append(FIN_SEPARATOR) 
          .append(MNC).append(FIN_SEPARATOR) 
          .append(LAC).append(FIN_SEPARATOR) 
          .append(CID);
        return sb.toString();
    }
    
    /**
     * @return [0:lon  1:lat]
     */
    private Double[] getGeoLocation(String MCC, String MNC, String LAC, String CID) {
        String queryMapKey = buildMapKey(MCC, MNC, LAC, CID);
        return JIZHAN_GEOLOCATION_MAP.get(queryMapKey);
    }

	public void setLocationGeoLight(LocationGeoLight locationGeoLight) {
		this.locationGeoLight = locationGeoLight;
	}
}
