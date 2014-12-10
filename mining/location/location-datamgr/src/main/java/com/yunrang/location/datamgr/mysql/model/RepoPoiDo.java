package com.yunrang.location.datamgr.mysql.model;

import java.io.Serializable;

public class RepoPoiDo implements Serializable {
    private Integer poiId;

    private String provinceName;

    private String provinceCode;

    private String cityName;

    private String cityCode;

    private String specAddress;

    private String rawAddress;

    private String tags;

    private Integer tupleStatus;

    private static final long serialVersionUID = 1L;

    public Integer getPoiId() {
        return poiId;
    }

    public void setPoiId(Integer poiId) {
        this.poiId = poiId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName == null ? null : provinceName.trim();
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode == null ? null : provinceCode.trim();
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    public String getSpecAddress() {
        return specAddress;
    }

    public void setSpecAddress(String specAddress) {
        this.specAddress = specAddress == null ? null : specAddress.trim();
    }

    public String getRawAddress() {
        return rawAddress;
    }

    public void setRawAddress(String rawAddress) {
        this.rawAddress = rawAddress == null ? null : rawAddress.trim();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    public Integer getTupleStatus() {
        return tupleStatus;
    }

    public void setTupleStatus(Integer tupleStatus) {
        this.tupleStatus = tupleStatus;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        RepoPoiDo other = (RepoPoiDo) that;
        return (this.getPoiId() == null ? other.getPoiId() == null : this.getPoiId().equals(other.getPoiId()))
            && (this.getProvinceName() == null ? other.getProvinceName() == null : this.getProvinceName().equals(other.getProvinceName()))
            && (this.getProvinceCode() == null ? other.getProvinceCode() == null : this.getProvinceCode().equals(other.getProvinceCode()))
            && (this.getCityName() == null ? other.getCityName() == null : this.getCityName().equals(other.getCityName()))
            && (this.getCityCode() == null ? other.getCityCode() == null : this.getCityCode().equals(other.getCityCode()))
            && (this.getSpecAddress() == null ? other.getSpecAddress() == null : this.getSpecAddress().equals(other.getSpecAddress()))
            && (this.getRawAddress() == null ? other.getRawAddress() == null : this.getRawAddress().equals(other.getRawAddress()))
            && (this.getTags() == null ? other.getTags() == null : this.getTags().equals(other.getTags()))
            && (this.getTupleStatus() == null ? other.getTupleStatus() == null : this.getTupleStatus().equals(other.getTupleStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPoiId() == null) ? 0 : getPoiId().hashCode());
        result = prime * result + ((getProvinceName() == null) ? 0 : getProvinceName().hashCode());
        result = prime * result + ((getProvinceCode() == null) ? 0 : getProvinceCode().hashCode());
        result = prime * result + ((getCityName() == null) ? 0 : getCityName().hashCode());
        result = prime * result + ((getCityCode() == null) ? 0 : getCityCode().hashCode());
        result = prime * result + ((getSpecAddress() == null) ? 0 : getSpecAddress().hashCode());
        result = prime * result + ((getRawAddress() == null) ? 0 : getRawAddress().hashCode());
        result = prime * result + ((getTags() == null) ? 0 : getTags().hashCode());
        result = prime * result + ((getTupleStatus() == null) ? 0 : getTupleStatus().hashCode());
        return result;
    }
}