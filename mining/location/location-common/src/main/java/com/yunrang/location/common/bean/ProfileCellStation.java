package com.yunrang.location.common.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ProfileCellStation {
	private String opt;
	private String mcc;
	private String mnc; 
	private String lac;
	private String cid;
	private double longitude;
	private double latitude;
	
	public boolean isEmpty() {
        return (mcc==null || mnc==null || lac==null || cid==null);
    }
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public String getMnc() {
		return mnc;
	}
	public void setMnc(String mnc) {
		this.mnc = mnc;
	}
	public String getLac() {
		return lac;
	}
	public void setLac(String lac) {
		this.lac = lac;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
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
}
