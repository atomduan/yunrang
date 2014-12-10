package com.yunrang.location.datamgr.mysql.model;

import java.io.Serializable;

public class RepoGeoDo implements Serializable {
    private Integer geoId;

    private Integer originGeoType;

    private Double originLongitude;

    private Double originLatitude;

    private Integer adjustGeoType;

    private Double adjustLongitude;

    private Double adjustLatitude;

    private Integer poiId;

    private Integer tupleStatus;

    private static final long serialVersionUID = 1L;

    public Integer getGeoId() {
        return geoId;
    }

    public void setGeoId(Integer geoId) {
        this.geoId = geoId;
    }

    public Integer getOriginGeoType() {
        return originGeoType;
    }

    public void setOriginGeoType(Integer originGeoType) {
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

    public Integer getAdjustGeoType() {
        return adjustGeoType;
    }

    public void setAdjustGeoType(Integer adjustGeoType) {
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

    public Integer getPoiId() {
        return poiId;
    }

    public void setPoiId(Integer poiId) {
        this.poiId = poiId;
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
        RepoGeoDo other = (RepoGeoDo) that;
        return (this.getGeoId() == null ? other.getGeoId() == null : this.getGeoId().equals(other.getGeoId()))
            && (this.getOriginGeoType() == null ? other.getOriginGeoType() == null : this.getOriginGeoType().equals(other.getOriginGeoType()))
            && (this.getOriginLongitude() == null ? other.getOriginLongitude() == null : this.getOriginLongitude().equals(other.getOriginLongitude()))
            && (this.getOriginLatitude() == null ? other.getOriginLatitude() == null : this.getOriginLatitude().equals(other.getOriginLatitude()))
            && (this.getAdjustGeoType() == null ? other.getAdjustGeoType() == null : this.getAdjustGeoType().equals(other.getAdjustGeoType()))
            && (this.getAdjustLongitude() == null ? other.getAdjustLongitude() == null : this.getAdjustLongitude().equals(other.getAdjustLongitude()))
            && (this.getAdjustLatitude() == null ? other.getAdjustLatitude() == null : this.getAdjustLatitude().equals(other.getAdjustLatitude()))
            && (this.getPoiId() == null ? other.getPoiId() == null : this.getPoiId().equals(other.getPoiId()))
            && (this.getTupleStatus() == null ? other.getTupleStatus() == null : this.getTupleStatus().equals(other.getTupleStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGeoId() == null) ? 0 : getGeoId().hashCode());
        result = prime * result + ((getOriginGeoType() == null) ? 0 : getOriginGeoType().hashCode());
        result = prime * result + ((getOriginLongitude() == null) ? 0 : getOriginLongitude().hashCode());
        result = prime * result + ((getOriginLatitude() == null) ? 0 : getOriginLatitude().hashCode());
        result = prime * result + ((getAdjustGeoType() == null) ? 0 : getAdjustGeoType().hashCode());
        result = prime * result + ((getAdjustLongitude() == null) ? 0 : getAdjustLongitude().hashCode());
        result = prime * result + ((getAdjustLatitude() == null) ? 0 : getAdjustLatitude().hashCode());
        result = prime * result + ((getPoiId() == null) ? 0 : getPoiId().hashCode());
        result = prime * result + ((getTupleStatus() == null) ? 0 : getTupleStatus().hashCode());
        return result;
    }
}