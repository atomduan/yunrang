package com.yunrang.location.datamgr.mysql.util;

import java.util.Date;

import com.yunrang.location.common.bean.ProfileCity;
import com.yunrang.location.common.bean.ProfileIpRange;
import com.yunrang.location.common.bean.ProfilePoi;
import com.yunrang.location.datamgr.mysql.model.RepoCellStationDo;
import com.yunrang.location.datamgr.mysql.model.RepoGeoDo;
import com.yunrang.location.datamgr.mysql.model.RepoIpRangeDo;
import com.yunrang.location.datamgr.mysql.model.RepoIpReferDo;
import com.yunrang.location.datamgr.mysql.model.RepoPoiDo;

public class UtilBeanToDoConverter {
	
	public static RepoGeoDo convert2GeoDo(ProfilePoi bean) {
		RepoGeoDo repoGeoDO = new RepoGeoDo();
		repoGeoDO.setAdjustGeoType(bean.getAdjustGeoType().getValue());
		repoGeoDO.setAdjustLongitude(bean.getAdjustLongitude());
		repoGeoDO.setAdjustLatitude(bean.getAdjustLatitude());
		repoGeoDO.setGeoId(bean.getGeoId());
		repoGeoDO.setOriginGeoType(bean.getOriginGeoType().getValue());
		repoGeoDO.setOriginLongitude(bean.getOriginLongitude());
		repoGeoDO.setOriginLatitude(bean.getOriginLatitude());
		repoGeoDO.setPoiId(bean.getPoiId());
		return repoGeoDO;
	}
	
	public static RepoCellStationDo convert2CellStationDo(ProfilePoi bean) {
		RepoCellStationDo cellStationDO = new RepoCellStationDo();
		cellStationDO.setAccuracy(bean.getCellStationAccuracy());
		cellStationDO.setCell(bean.getCell());
		cellStationDO.setCellStationId(bean.getCellStationId());
		cellStationDO.setGeoId(bean.getGeoId());
		cellStationDO.setLac(bean.getLac());
		cellStationDO.setMcc(bean.getMcc());
		cellStationDO.setMnc(bean.getMnc());
		return cellStationDO;
	}
	
    public static RepoIpRangeDo convert2IpRangeDo(ProfileIpRange locationProfileIpRange) {
    	RepoIpRangeDo ipRangeDo = new RepoIpRangeDo();
        ipRangeDo.setIpRangeId(locationProfileIpRange.getIpRangeId());
        ipRangeDo.setRet(locationProfileIpRange.getRet());
        ipRangeDo.setIpStrStart(locationProfileIpRange.getIpStrStart());
        ipRangeDo.setIpStrStartNum(locationProfileIpRange.getIpStrStartNum());
        ipRangeDo.setIpStrEnd(locationProfileIpRange.getIpStrEnd());
        ipRangeDo.setIpStrEndNum(locationProfileIpRange.getIpStrEndNum());
        ipRangeDo.setProvinceCode(locationProfileIpRange.getProvinceCode());
        ipRangeDo.setProvinceName(locationProfileIpRange.getProvinceName());
        ipRangeDo.setCityCode(locationProfileIpRange.getCityCode());
        ipRangeDo.setCityName(locationProfileIpRange.getCityName());
        ipRangeDo.setDistrictCode(locationProfileIpRange.getDistrictCode());
        ipRangeDo.setDistrictName(locationProfileIpRange.getDistrictName());
        ipRangeDo.setIsp(locationProfileIpRange.getIsp());
        ipRangeDo.setType(locationProfileIpRange.getType());
        ipRangeDo.setDescription(locationProfileIpRange.getDescription());
        ipRangeDo.setUpdateTime(new Date());
        return ipRangeDo;
    }
    
    public static RepoIpReferDo convert2IpReferAsPossible(ProfileCity profileCity) {
    	RepoIpReferDo ipReferDo = new RepoIpReferDo();
        ipReferDo.setProvinceCode(profileCity.getProvinceCode());
        ipReferDo.setProvinceName(profileCity.getProvinceName());
        ipReferDo.setCityCode(profileCity.getCityCode());
        ipReferDo.setCityName(profileCity.getCityName());
        ipReferDo.setDistrictCode(profileCity.getDistrictCode());
        ipReferDo.setDistrictName(profileCity.getDistrictName());
        ipReferDo.setUpdateTime(new Date());
        return ipReferDo;
    }
    
    public static RepoIpReferDo convert2IpReferDo(ProfilePoi locationProfilePoi) {
    	RepoIpReferDo ipReferDo = new RepoIpReferDo();
        ipReferDo.setIpReferId(locationProfilePoi.getIpReferId());
        ipReferDo.setIpStr(locationProfilePoi.getIpStr());
        ipReferDo.setProvinceCode(locationProfilePoi.getProvinceCode());
        ipReferDo.setProvinceName(locationProfilePoi.getProvinceName());
        ipReferDo.setCityCode(locationProfilePoi.getCityCode());
        ipReferDo.setCityName(locationProfilePoi.getCityName());
        ipReferDo.setDistrictCode(locationProfilePoi.getDistrictCode());
        ipReferDo.setDistrictName(locationProfilePoi.getDistrictName());
        return ipReferDo;
    }
    
    public static RepoPoiDo convert2PoiDo(ProfilePoi locationProfilePoi) {
    	RepoPoiDo poiDO = new RepoPoiDo();
    	poiDO.setCityCode(locationProfilePoi.getCityCode());
    	poiDO.setCityName(locationProfilePoi.getCityName());
    	poiDO.setPoiId(locationProfilePoi.getPoiId());
    	poiDO.setProvinceCode(locationProfilePoi.getProvinceCode());
    	poiDO.setProvinceName(locationProfilePoi.getProvinceName());
    	poiDO.setRawAddress(locationProfilePoi.getRawAddress());
    	poiDO.setSpecAddress(locationProfilePoi.getPoiSpecAddress());
    	poiDO.setTags(locationProfilePoi.getTags());
    	return poiDO;
    }
}
