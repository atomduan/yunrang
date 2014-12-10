package com.yunrang.location.common.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.yunrang.location.common.enums.GeoCoordinateType;

public class ProfilePoi {
    private Integer mcc = -1;
    private Integer mnc = -1;
    private Integer lac = -1;
    private Integer cell = -1;
    private Integer cellStationAccuracy = -1;
    private GeoCoordinateType originGeoType = GeoCoordinateType.Undefined_Coordinate;
    private Double originLongitude = -1.0;
    private Double originLatitude = -1.0;
    private GeoCoordinateType adjustGeoType = GeoCoordinateType.Undefined_Coordinate;
    private Double adjustLongitude = -1.0;
    private Double adjustLatitude = -1.0;
    
    private String provinceName;
    private String provinceCode;
    private String cityName;
    private String cityCode;
    private String districtName;
    private String districtCode;
    private String poiSpecAddress;
    private String rawAddress;
    private String ipStr;
    private String ipSpecAddress;
    private String tags;
    
    private Integer cellStationId;
    private Integer geoId;
    private Integer poiId;
    private Integer ipReferId;
    

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    public Integer getCellStationId() {
		return cellStationId;
	}
	public void setCellStationId(Integer cellStationId) {
		this.cellStationId = cellStationId;
	}
	public Integer getGeoId() {
		return geoId;
	}
	public void setGeoId(Integer geoId) {
		this.geoId = geoId;
	}
	public Integer getPoiId() {
		return poiId;
	}
	public void setPoiId(Integer poiId) {
		this.poiId = poiId;
	}
	public Integer getIpReferId() {
		return ipReferId;
	}
	public void setIpReferId(Integer ipReferId) {
		this.ipReferId = ipReferId;
	}
	public String getIpStr() {
        return ipStr;
    }
    public void setIpStr(String ipStr) {
        this.ipStr = ipStr;
    }
    public Integer getMcc() {
        return mcc;
    }
    public void setMcc(Integer mcc) {
        this.mcc = mcc;
    }
    public Integer getMnc() {
        return mnc;
    }
    public void setMnc(Integer mnc) {
        this.mnc = mnc;
    }
    public Integer getLac() {
        return lac;
    }
    public void setLac(Integer lac) {
        this.lac = lac;
    }
    public Integer getCell() {
        return cell;
    }
    public void setCell(Integer cell) {
        this.cell = cell;
    }
    public GeoCoordinateType getOriginGeoType() {
        return originGeoType;
    }
    public void setOriginGeoType(GeoCoordinateType originGeoType) {
        this.originGeoType = originGeoType;
    }
    public Double getOriginLongitude() {
        return originLongitude;
    }
    public void setOriginLongitude(Double originLongitude) {
        this.originLongitude = originLongitude;
    }
    public Double getOriginLatitude() {
        return originLatitude;
    }
    public void setOriginLatitude(Double originLatitude) {
        this.originLatitude = originLatitude;
    }
    public GeoCoordinateType getAdjustGeoType() {
        return adjustGeoType;
    }
    public void setAdjustGeoType(GeoCoordinateType adjustGeoType) {
        this.adjustGeoType = adjustGeoType;
    }
    public Double getAdjustLongitude() {
        return adjustLongitude;
    }
    public void setAdjustLongitude(Double adjustLongitude) {
        this.adjustLongitude = adjustLongitude;
    }
    public Double getAdjustLatitude() {
        return adjustLatitude;
    }
    public void setAdjustLatitude(Double adjustLatitude) {
        this.adjustLatitude = adjustLatitude;
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
    public String getPoiSpecAddress() {
        return poiSpecAddress;
    }
    public void setPoiSpecAddress(String poiSpecAddress) {
        this.poiSpecAddress = poiSpecAddress;
    }
    public String getRawAddress() {
        return rawAddress;
    }
    public void setRawAddress(String rawAddress) {
        this.rawAddress = rawAddress;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getTags() {
        return tags;
    }
    public void setCellStationAccuracy(Integer cellStationAccuracy) {
        this.cellStationAccuracy  = cellStationAccuracy;
    }
    public int getCellStationAccuracy() {
        return cellStationAccuracy;
    }
    public String getIpSpecAddress() {
        return ipSpecAddress;
    }
    public void setIpSpecAddress(String ipSpecAddress) {
        this.ipSpecAddress = ipSpecAddress;
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
}
