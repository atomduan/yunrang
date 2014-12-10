package com.yunrang.location.integration.support.file.history.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunrang.location.common.bean.ProfileCity;
import com.yunrang.location.common.bean.ProfilePoi;
import com.yunrang.location.common.context.ContextPoiQuery;
import com.yunrang.location.common.enums.GeoCoordinateType;
import com.yunrang.location.datamgr.mysql.dao.RepoCellStationDao;
import com.yunrang.location.datamgr.mysql.dao.RepoGeoDao;
import com.yunrang.location.datamgr.mysql.dao.RepoPoiDao;

/**
 * 460,2,3183,34872,"121.739185","31.048152","121.743535543022","31.0460286796887","811","中国上海市浦东新区惠南西门路"
 * 460,2,3183,35571,"121.770509","31.05201","121.774854178604","31.0499005531497","810","中国上海市浦东新区人民东路2246号-2370号"
 * 460,2,3181,35753,"121.522338","31.195696","121.526704636276","31.193617277859","814","中国上海市浦东新区严杨路28号 邮政编码: 200125"
 * 460,2,3183,35882,"121.724115","31.032933","121.728454814186","31.030790953372","809","中国上海市浦东新区汇成路530号"
 * 460,2,3183,35892,"121.745046","31.026209","121.74940190744","31.0240805932766","812","中国上海市浦东新区宣黄公路2666号-2870号"
 * 460,2,2184,35903,"121.739504","31.06682","121.743854543022","31.064706286427","811","中国上海市浦东新区听乐路"
 * 460,2,2184,35911,"121.736398","31.080233","121.74075390744","31.0781333678773","812","中国上海市浦东新区大川公路"
 * 460,2,2184,35913,"121.72501","31.070009","121.729355178604","31.0678907623773","810","中国上海市浦东新区下盐公路"
 */
public class TopwangCellStationUpdater {
	private static Logger logger = LoggerFactory.getLogger(TopwangCellStationUpdater.class);
	
	private RepoPoiDao repoPoiDao;
	private RepoGeoDao repoGeoDao;
	private RepoCellStationDao repoCellStationDao;
	
	private ContextPoiQuery contextPoiQuery;
	
	public void doUpdate(String line) throws Exception {
		logger.info("on processing record:["+line+"]");
		ProfilePoi location = parse(line);
		int affectedPoiId = repoPoiDao.selectFristMatchedPoiId(
				location.getProvinceName(), location.getCityName(), location.getPoiSpecAddress());
		if (affectedPoiId <= 0) {
			affectedPoiId = repoPoiDao.insertSelective(location);
		}
		location.setPoiId(affectedPoiId);
		int affectedGeoId = repoGeoDao.selectFristMatchedGeoId(
				location.getOriginLongitude(), location.getOriginLatitude(), location.getAdjustLongitude(), location.getAdjustLatitude());
		if (affectedGeoId <= 0) {
			affectedGeoId = repoGeoDao.insertSelective(location);
		}
		location.setGeoId(affectedGeoId);
		int affectedCellStationId = repoCellStationDao.selectFristMatchedCellStationId(location.getMcc(), location.getMnc(), location.getLac(), location.getCell());
		if (affectedCellStationId <= 0) {
			repoCellStationDao.insertSelective(location);
		} else {
			location.setCellStationId(affectedCellStationId);
			repoCellStationDao.updateByPrimaryKeySelective(location);
		}
	}
	
	private ProfilePoi parse(String line) {
        String[] fields = StringUtils.trimToNull(line).replace("\"", "").split(",");
        Integer mcc = Integer.parseInt(fields[0]);
        Integer mnc = Integer.parseInt(fields[1]);
        Integer lac = Integer.parseInt(fields[2]);
        Integer cell = Integer.parseInt(fields[3]);
        Double raw_longitude = Double.parseDouble(fields[4]);
        Double raw_latitude = Double.parseDouble(fields[5]);
        Double google_longitude = Double.parseDouble(fields[6]);
        Double google_latitude = Double.parseDouble(fields[7]);
        Integer cellStationAccuracy;
        try {
            cellStationAccuracy = Integer.parseInt(fields[8]);
        } catch (Exception e) {
            cellStationAccuracy = -1; // -1 means undefined....
        }
        String rawAddress = fields[9].trim();
        ProfileCity profile = contextPoiQuery.resolveToCityProfile(rawAddress);
        ProfilePoi location = new ProfilePoi();
        location.setMcc(mcc);
        location.setMnc(mnc);
        location.setLac(lac);
        location.setCell(cell);
        location.setCellStationAccuracy(cellStationAccuracy);
        location.setOriginGeoType(GeoCoordinateType.Raw_Coordinate);
        location.setOriginLongitude(raw_longitude);
        location.setOriginLatitude(raw_latitude);
        location.setAdjustGeoType(GeoCoordinateType.Google_Coordinate);
        location.setAdjustLongitude(google_longitude);
        location.setAdjustLatitude(google_latitude);
        location.setProvinceName(profile.getProvinceName());
        location.setProvinceCode(profile.getProvinceCode());
        location.setCityName(profile.getCityName());
        location.setCityCode(profile.getCityCode());
        location.setPoiSpecAddress(profile.getOptionalInfo());
        location.setRawAddress(rawAddress);
        return location;
    }

	public void setRepoPoiDao(RepoPoiDao repoPoiDao) {
		this.repoPoiDao = repoPoiDao;
	}
	public void setRepoGeoDao(RepoGeoDao repoGeoDao) {
		this.repoGeoDao = repoGeoDao;
	}
	public void setRepoCellStationDao(RepoCellStationDao repoCellStationDao) {
		this.repoCellStationDao = repoCellStationDao;
	}
	public void setContextPoiQuery(ContextPoiQuery contextPoiQuery) {
		this.contextPoiQuery = contextPoiQuery;
	}
}