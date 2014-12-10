package com.yunrang.location.datamgr.mysql.model;

import java.io.Serializable;
import java.util.Date;

public class RepoIpReferDo implements Serializable {
    private Integer ipReferId;

    private String ipStr;

    private String provinceCode;

    private String provinceName;

    private String cityCode;

    private String cityName;

    private String districtCode;

    private String districtName;

    private Integer ipRangeId;

    private Integer tupleStatus;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

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
        this.ipStr = ipStr == null ? null : ipStr.trim();
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode == null ? null : provinceCode.trim();
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName == null ? null : provinceName.trim();
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode == null ? null : districtCode.trim();
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName == null ? null : districtName.trim();
    }

    public Integer getIpRangeId() {
        return ipRangeId;
    }

    public void setIpRangeId(Integer ipRangeId) {
        this.ipRangeId = ipRangeId;
    }

    public Integer getTupleStatus() {
        return tupleStatus;
    }

    public void setTupleStatus(Integer tupleStatus) {
        this.tupleStatus = tupleStatus;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
        RepoIpReferDo other = (RepoIpReferDo) that;
        return (this.getIpReferId() == null ? other.getIpReferId() == null : this.getIpReferId().equals(other.getIpReferId()))
            && (this.getIpStr() == null ? other.getIpStr() == null : this.getIpStr().equals(other.getIpStr()))
            && (this.getProvinceCode() == null ? other.getProvinceCode() == null : this.getProvinceCode().equals(other.getProvinceCode()))
            && (this.getProvinceName() == null ? other.getProvinceName() == null : this.getProvinceName().equals(other.getProvinceName()))
            && (this.getCityCode() == null ? other.getCityCode() == null : this.getCityCode().equals(other.getCityCode()))
            && (this.getCityName() == null ? other.getCityName() == null : this.getCityName().equals(other.getCityName()))
            && (this.getDistrictCode() == null ? other.getDistrictCode() == null : this.getDistrictCode().equals(other.getDistrictCode()))
            && (this.getDistrictName() == null ? other.getDistrictName() == null : this.getDistrictName().equals(other.getDistrictName()))
            && (this.getIpRangeId() == null ? other.getIpRangeId() == null : this.getIpRangeId().equals(other.getIpRangeId()))
            && (this.getTupleStatus() == null ? other.getTupleStatus() == null : this.getTupleStatus().equals(other.getTupleStatus()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getIpReferId() == null) ? 0 : getIpReferId().hashCode());
        result = prime * result + ((getIpStr() == null) ? 0 : getIpStr().hashCode());
        result = prime * result + ((getProvinceCode() == null) ? 0 : getProvinceCode().hashCode());
        result = prime * result + ((getProvinceName() == null) ? 0 : getProvinceName().hashCode());
        result = prime * result + ((getCityCode() == null) ? 0 : getCityCode().hashCode());
        result = prime * result + ((getCityName() == null) ? 0 : getCityName().hashCode());
        result = prime * result + ((getDistrictCode() == null) ? 0 : getDistrictCode().hashCode());
        result = prime * result + ((getDistrictName() == null) ? 0 : getDistrictName().hashCode());
        result = prime * result + ((getIpRangeId() == null) ? 0 : getIpRangeId().hashCode());
        result = prime * result + ((getTupleStatus() == null) ? 0 : getTupleStatus().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}