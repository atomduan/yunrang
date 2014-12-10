package com.yunrang.location.service.support;

import java.util.List;

import com.yunrang.location.service.support.cell.LocationCellLight;
import com.yunrang.location.service.support.geo.LocationGeoLight;
import com.yunrang.location.service.support.ip.LocationSinaIpRange;


public class LocationService {
    private LocationSinaIpRange locationSinaIpRange;
    private LocationGeoLight locationGeoLight;
    private LocationCellLight locationCellLight;
    
    public List<String> getProvinceAndCity(String ip) {
        return locationSinaIpRange.getProvinceAndCity(ip);
    }
    
    public List<String> getProvinceAndCity(double lon, double lat) {
        return locationGeoLight.getProvinceAndCity(lon, lat);
    }
    
    public List<String> getProvinceAndCity(String NetworkOperator, String LAC, String CID) {
        return locationCellLight.getProvinceAndCity(NetworkOperator, LAC, CID);
    }

	public void setLocationSinaIpRange(LocationSinaIpRange locationSinaIpRange) {
		this.locationSinaIpRange = locationSinaIpRange;
	}

	public void setLocationGeoLight(LocationGeoLight locationGeoLight) {
		this.locationGeoLight = locationGeoLight;
	}

	public void setLocationCellLight(LocationCellLight locationCellLight) {
		this.locationCellLight = locationCellLight;
	}
}
