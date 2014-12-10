package com.yunrang.location.common.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class ProfileIpRange {
    private Integer ipRangeId;
    private String ret;
    private String ipStrStart;
    private Long ipStrStartNum;
    private String ipStrEnd;
    private Long ipStrEndNum;
    private String countryName;
    private String provinceName;
    private String provinceCode;
    private String cityName;
    private String cityCode;
    private String districtName;
    private String districtCode;
    private String isp;
    private String type;
    private String description;
    private String updateTime;
    
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public Integer getIpRangeId() {
		return ipRangeId;
	}
	public void setIpRangeId(Integer ipRangeId) {
		this.ipRangeId = ipRangeId;
	}
	public String getRet() {
        return ret;
    }
    public void setRet(String ret) {
        this.ret = ret;
    }
    public String getIpStrStart() {
        return ipStrStart;
    }
    public void setIpStrStart(String ipStrStart) {
        this.ipStrStart = ipStrStart;
    }
    public Long getIpStrStartNum() {
        return ipStrStartNum;
    }
    public void setIpStrStartNum(Long ipStrStartNum) {
        this.ipStrStartNum = ipStrStartNum;
    }
    public String getIpStrEnd() {
        return ipStrEnd;
    }
    public void setIpStrEnd(String ipStrEnd) {
        this.ipStrEnd = ipStrEnd;
    }
    public Long getIpStrEndNum() {
        return ipStrEndNum;
    }
    public void setIpStrEndNum(Long ipStrEndNum) {
        this.ipStrEndNum = ipStrEndNum;
    }
    public String getCountryName() {
        return countryName;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
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
    public String getIsp() {
        return isp;
    }
    public void setIsp(String isp) {
        this.isp = isp;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
