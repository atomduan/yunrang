package com.yunrang.location.datamgr.mysql.model;

import java.io.Serializable;
import java.util.Date;

public class RepoIpRangeDo implements Serializable {
    private Integer ipRangeId;

    private String ret;

    private String ipStrStart;

    private Long ipStrStartNum;

    private String ipStrEnd;

    private Long ipStrEndNum;

    private String provinceName;

    private String provinceCode;

    private String cityName;

    private String cityCode;

    private String districtName;

    private String districtCode;

    private String isp;

    private String type;

    private String description;

    private String referFullCode;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

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
        this.ret = ret == null ? null : ret.trim();
    }

    public String getIpStrStart() {
        return ipStrStart;
    }

    public void setIpStrStart(String ipStrStart) {
        this.ipStrStart = ipStrStart == null ? null : ipStrStart.trim();
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
        this.ipStrEnd = ipStrEnd == null ? null : ipStrEnd.trim();
    }

    public Long getIpStrEndNum() {
        return ipStrEndNum;
    }

    public void setIpStrEndNum(Long ipStrEndNum) {
        this.ipStrEndNum = ipStrEndNum;
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

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName == null ? null : districtName.trim();
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode == null ? null : districtCode.trim();
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp == null ? null : isp.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getReferFullCode() {
        return referFullCode;
    }

    public void setReferFullCode(String referFullCode) {
        this.referFullCode = referFullCode == null ? null : referFullCode.trim();
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
        RepoIpRangeDo other = (RepoIpRangeDo) that;
        return (this.getIpRangeId() == null ? other.getIpRangeId() == null : this.getIpRangeId().equals(other.getIpRangeId()))
            && (this.getRet() == null ? other.getRet() == null : this.getRet().equals(other.getRet()))
            && (this.getIpStrStart() == null ? other.getIpStrStart() == null : this.getIpStrStart().equals(other.getIpStrStart()))
            && (this.getIpStrStartNum() == null ? other.getIpStrStartNum() == null : this.getIpStrStartNum().equals(other.getIpStrStartNum()))
            && (this.getIpStrEnd() == null ? other.getIpStrEnd() == null : this.getIpStrEnd().equals(other.getIpStrEnd()))
            && (this.getIpStrEndNum() == null ? other.getIpStrEndNum() == null : this.getIpStrEndNum().equals(other.getIpStrEndNum()))
            && (this.getProvinceName() == null ? other.getProvinceName() == null : this.getProvinceName().equals(other.getProvinceName()))
            && (this.getProvinceCode() == null ? other.getProvinceCode() == null : this.getProvinceCode().equals(other.getProvinceCode()))
            && (this.getCityName() == null ? other.getCityName() == null : this.getCityName().equals(other.getCityName()))
            && (this.getCityCode() == null ? other.getCityCode() == null : this.getCityCode().equals(other.getCityCode()))
            && (this.getDistrictName() == null ? other.getDistrictName() == null : this.getDistrictName().equals(other.getDistrictName()))
            && (this.getDistrictCode() == null ? other.getDistrictCode() == null : this.getDistrictCode().equals(other.getDistrictCode()))
            && (this.getIsp() == null ? other.getIsp() == null : this.getIsp().equals(other.getIsp()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getReferFullCode() == null ? other.getReferFullCode() == null : this.getReferFullCode().equals(other.getReferFullCode()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getIpRangeId() == null) ? 0 : getIpRangeId().hashCode());
        result = prime * result + ((getRet() == null) ? 0 : getRet().hashCode());
        result = prime * result + ((getIpStrStart() == null) ? 0 : getIpStrStart().hashCode());
        result = prime * result + ((getIpStrStartNum() == null) ? 0 : getIpStrStartNum().hashCode());
        result = prime * result + ((getIpStrEnd() == null) ? 0 : getIpStrEnd().hashCode());
        result = prime * result + ((getIpStrEndNum() == null) ? 0 : getIpStrEndNum().hashCode());
        result = prime * result + ((getProvinceName() == null) ? 0 : getProvinceName().hashCode());
        result = prime * result + ((getProvinceCode() == null) ? 0 : getProvinceCode().hashCode());
        result = prime * result + ((getCityName() == null) ? 0 : getCityName().hashCode());
        result = prime * result + ((getCityCode() == null) ? 0 : getCityCode().hashCode());
        result = prime * result + ((getDistrictName() == null) ? 0 : getDistrictName().hashCode());
        result = prime * result + ((getDistrictCode() == null) ? 0 : getDistrictCode().hashCode());
        result = prime * result + ((getIsp() == null) ? 0 : getIsp().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getReferFullCode() == null) ? 0 : getReferFullCode().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}