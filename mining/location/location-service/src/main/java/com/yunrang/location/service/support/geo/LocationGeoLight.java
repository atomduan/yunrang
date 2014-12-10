package com.yunrang.location.service.support.geo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

import com.yunrang.location.common.bean.ProfileCity;
import com.yunrang.location.common.config.GeoCityConfig;
import com.yunrang.location.common.context.ContextPoiQuery;
import com.yunrang.location.service.util.UtilLocationResult;

public class LocationGeoLight implements InitializingBean {
    
    private Map<String, GeoCityProfile> fullCityCodeToCitiesMap;
    private Map<Double, List<GeoCityProfile>> longitudeCityMap;
    private Map<Double, List<GeoCityProfile>> latitudeCityMap;
    private Double[] ascLatitudes;
    private Double[] ascLongitudes;
    
    private ContextPoiQuery contextPoiQuery;
    
    @Override
	public void afterPropertiesSet() throws Exception {
    	doInitialization();
	}
    
    private void doInitialization() {
        this.longitudeCityMap = new HashMap<Double, List<GeoCityProfile>>();
        this.latitudeCityMap = new HashMap<Double, List<GeoCityProfile>>();
        this.ascLatitudes = new Double[GeoCityConfig.LAT_SORTED_CITIES.length];
        this.ascLongitudes = new Double[GeoCityConfig.LNG_SORTED_CITIES.length];
        this.fullCityCodeToCitiesMap = new HashMap<String, GeoCityProfile>();
        
        for (int i=0; i<GeoCityConfig.LNG_SORTED_CITIES.length; i++) {
            String[] params = GeoCityConfig.LNG_SORTED_CITIES[i].split(";");
            Double longitude = Double.parseDouble(params[0]);
            Double latitude = Double.parseDouble(params[1]);
            String fullCityCode = params[2];
            GeoCityProfile geoCityProfile = new GeoCityProfile(longitude, latitude, fullCityCode);
            fullCityCodeToCitiesMap.put(fullCityCode, geoCityProfile);
            ascLongitudes[i] = longitude;
            if (longitudeCityMap.containsKey(longitude) == false) {
                longitudeCityMap.put(longitude, new ArrayList<GeoCityProfile>());
            }
            longitudeCityMap.get(longitude).add(geoCityProfile );
        }
        for (int i=0; i<GeoCityConfig.LAT_SORTED_CITIES.length; i++) {
            String[] eles = GeoCityConfig.LAT_SORTED_CITIES[i].split(";");
            Double longitude = Double.parseDouble(eles[1]);
            Double latitude = Double.parseDouble(eles[0]);
            String fullCityCode = eles[2];
            if (fullCityCodeToCitiesMap.containsKey(fullCityCode) == false) {
                fullCityCodeToCitiesMap.put(fullCityCode, new GeoCityProfile(longitude, latitude, fullCityCode));
            }
            GeoCityProfile geoCityProfile = fullCityCodeToCitiesMap.get(fullCityCode);
            ascLatitudes[i] = latitude;
            if (latitudeCityMap.containsKey(latitude) == false) {
                latitudeCityMap.put(latitude, new ArrayList<GeoCityProfile>());
            }
            latitudeCityMap.get(latitude).add(geoCityProfile);
        }
    }
    
    public List<String> getProvinceAndCity(Double longitude, Double latitude){
        ProfileCity locationProfileCity = getLocationProfileCityBean(longitude, latitude);
        String provinceName = locationProfileCity.getProvinceName();
        String cityName = locationProfileCity.getCityName();
        String districtName = locationProfileCity.getDistrictName();
        return UtilLocationResult.getProvinceAndCityListResult(provinceName, cityName, districtName);
    }
    
    public ProfileCity getLocationProfileCityBean(Double longitude, Double latitude) {
        GeoCityProfile geoCityProfile = resolve(longitude,latitude);
        String fullCityCode = geoCityProfile.getFullCityCode();
        String provinceCode = fullCityCode.substring(0,2);
        String cityCode = fullCityCode.substring(0,4);
        String districtCode = fullCityCode;
        
        ProfileCity profileCity = new ProfileCity();
        profileCity.setLongitude(longitude);
        profileCity.setLatitude(latitude);
        profileCity.setProvinceCode(provinceCode);
        profileCity.setProvinceName(contextPoiQuery.getProvinceCanonicalName(provinceCode));
        profileCity.setCityCode(cityCode);
        profileCity.setCityName(contextPoiQuery.getCityCanonicalName(cityCode));
        profileCity.setDistrictCode(districtCode);
        profileCity.setDistrictName(contextPoiQuery.getDistrictCanonicalName(districtCode));
        return profileCity;
    }
    
    private GeoCityProfile resolve(Double longitude, Double latitude) {
        Integer neighorRange = 60;
        // Note:get close cities base on different dims (latitude and longitude)
        List<GeoCityProfile> latCities = getCloseCities(neighorRange, latitude, latitudeCityMap, ascLatitudes);
        List<GeoCityProfile> lngCities = getCloseCities(neighorRange, longitude, longitudeCityMap, ascLongitudes);
        Set<GeoCityProfile> lngSet = new HashSet<GeoCityProfile>(lngCities);
        // Warning~~!!!: The corner cases do exist: the closest city neither in latCloseCities nor lngCloseCities.
        List<GeoCityProfile> rcdCities = new ArrayList<GeoCityProfile>();
        for (GeoCityProfile latcity : latCities) {
            if (lngSet.contains(latcity)) {
                rcdCities.add(latcity);
            }
        }
        // find the closest one
        return getTheClosestCity(longitude, latitude, rcdCities);
    }

    private GeoCityProfile getTheClosestCity(Double qlng, Double qlat, List<GeoCityProfile> cities) {
        GeoCityProfile closestCity = null;
        Double maxRange = Double.MAX_VALUE;
        // get the closed
        for (GeoCityProfile c : cities) {
            Double range = Math.sqrt( (c.getLatitude()-qlat)*(c.getLatitude()-qlat) + (c.getLongitude()-qlng)*(c.getLongitude()-qlng) );
            if (range < maxRange) {
                maxRange = range;
                closestCity = c;
            }
        }
        return closestCity;
    }
    
    private List<GeoCityProfile> getCloseCities(Integer range, Double q, Map<Double, List<GeoCityProfile>> dimCityMap, Double[] indexQ) {
        Integer[] root = new Integer[2];
        findSlot(indexQ, q, 0, indexQ.length, root);
        List<GeoCityProfile> closeCities = new ArrayList<GeoCityProfile>();
        for (int i=(root[0]-range); i <(root[1]+range); i++) {
            if (i < 0) continue;
            if (i > indexQ.length-1) break;
            List<GeoCityProfile> cities = dimCityMap.get(indexQ[i]);
            for (int j=0; j<cities.size(); j++) {
                GeoCityProfile c = cities.get(j);
                closeCities.add(c);
            }
        }
        return closeCities;
    }
    
    private void findSlot(Double[] target, Double q, int b, int e, Integer[] r) {
        // terminate condition
        if (b == e || b+1 == e) {
            r[0] = b; r[1] = e;
            return;
        } 
        // layer processes
        int p = (b+e)/2;
        if (target[p] > q) {
            findSlot(target, q, b, p, r);
        } else {
            findSlot(target, q, p, e, r);
        }
    }
    
    class GeoCityProfile {
        private Double longitude;
        private Double latitude;
        private String fullCityCode;
        private GeoCityProfile(Double longitude,Double latitude,String fullCityCode) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.fullCityCode = fullCityCode;
        }
        private Double getLongitude() {
            return longitude;
        }
        private Double getLatitude() {
            return latitude;
        }
        private String getFullCityCode() {
            return fullCityCode;
        }
    }

	public void setContextPoiQuery(ContextPoiQuery contextPoiQuery) {
		this.contextPoiQuery = contextPoiQuery;
	}
}
