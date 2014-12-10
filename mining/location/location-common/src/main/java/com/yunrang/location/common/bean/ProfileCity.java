package com.yunrang.location.common.bean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ProfileCity {
	private double longitude;
	private double latitude;
	private double accuracy;
    private String provinceName;
    private String provinceCode;
    private String cityName;
    private String cityCode;
    private String districtName;
    private String districtCode;
    private String optionalInfo;
    
    public boolean isEmpty() {
        return (provinceName==null && provinceCode==null && cityName==null && cityCode==null 
                	&& districtName==null && districtCode==null && optionalInfo==null);
    }
    
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
    public boolean equals(Object c) {
    	if (c instanceof ProfileCity) {
    		ProfileCity p = (ProfileCity)c;
    		boolean provinceEqual = StringUtils.equals(provinceCode, p.getProvinceCode());
    		boolean cityEqual = StringUtils.equals(cityCode, p.getCityCode());
    		boolean districtEqual = StringUtils.equals(districtCode, p.getDistrictCode());
    		return provinceEqual && cityEqual && districtEqual;
    	}
		return false;
    }
    
    public String getFullLocationCode() {
    	if (!StringUtils.isBlank(this.districtCode)){
    		return this.districtCode;
    	} else 
    	if (!StringUtils.isBlank(this.cityCode)) {
    		return this.cityCode+"00";
    	} else 
    	if (!StringUtils.isBlank(this.provinceCode)) {
    		return this.provinceCode+"0000";
    	} else {
    		return null;
    	}
    }
    public double getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getOptionalInfo() {
        return optionalInfo;
    }
    public void setOptionalInfo(String optionalInfo) {
        this.optionalInfo = optionalInfo;
    }
    public String getDistrictName() {
        return districtName;
    }
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
    public String getDistrictCode() {
        return districtCode;
    }
    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getCityCode() {
        return cityCode;
    }
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
    public String getProvinceName() {
        return provinceName;
    }
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
    public String getProvinceCode() {
        return provinceCode;
    }
    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
