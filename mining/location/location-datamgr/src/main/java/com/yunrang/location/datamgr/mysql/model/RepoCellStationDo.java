package com.yunrang.location.datamgr.mysql.model;

import java.io.Serializable;

public class RepoCellStationDo implements Serializable {
    private Integer cellStationId;

    private Integer mcc;

    private Integer mnc;

    private Integer lac;

    private Integer cell;

    private Integer accuracy;

    private Integer geoId;

    private Integer tupleStatus;

    private static final long serialVersionUID = 1L;

    public Integer getCellStationId() {
        return cellStationId;
    }

    public void setCellStationId(Integer cellStationId) {
        this.cellStationId = cellStationId;
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

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getGeoId() {
        return geoId;
    }

    public void setGeoId(Integer geoId) {
        this.geoId = geoId;
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
        RepoCellStationDo other = (RepoCellStationDo) that;
        return (this.getCellStationId() == null ? other.getCellStationId() == null : this.getCellStationId().equals(other.getCellStationId()))
            && (this.getMcc() == null ? other.getMcc() == null : this.getMcc().equals(other.getMcc()))
            && (this.getMnc() == null ? other.getMnc() == null : this.getMnc().equals(other.getMnc()))
            && (this.getLac() == null ? other.getLac() == null : this.getLac().equals(other.getLac()))
            && (this.getCell() == null ? other.getCell() == null : this.getCell().equals(other.getCell()))
            && (this.getAccuracy() == null ? other.getAccuracy() == null : this.getAccuracy().equals(other.getAccuracy()))
            && (this.getGeoId() == null ? other.getGeoId() == null : this.getGeoId().equals(other.getGeoId()))
            && (this.getTupleStatus() == null ? other.getTupleStatus() == null : this.getTupleStatus().equals(other.getTupleStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCellStationId() == null) ? 0 : getCellStationId().hashCode());
        result = prime * result + ((getMcc() == null) ? 0 : getMcc().hashCode());
        result = prime * result + ((getMnc() == null) ? 0 : getMnc().hashCode());
        result = prime * result + ((getLac() == null) ? 0 : getLac().hashCode());
        result = prime * result + ((getCell() == null) ? 0 : getCell().hashCode());
        result = prime * result + ((getAccuracy() == null) ? 0 : getAccuracy().hashCode());
        result = prime * result + ((getGeoId() == null) ? 0 : getGeoId().hashCode());
        result = prime * result + ((getTupleStatus() == null) ? 0 : getTupleStatus().hashCode());
        return result;
    }
}