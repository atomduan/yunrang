package com.yunrang.location.integration.support.file.history.data_201306;



import java.sql.SQLException;

import com.yunrang.location.common.bean.ProfileCity;
import com.yunrang.location.common.bean.ProfilePoi;
import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.common.context.ContextPoiQuery;
import com.yunrang.location.common.enums.GeoCoordinateType;
import com.yunrang.location.datamgr.mysql.dao.RepoCellStationDao;
import com.yunrang.location.datamgr.mysql.dao.RepoGeoDao;
import com.yunrang.location.datamgr.mysql.dao.RepoPoiDao;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;


/**
1,460,0,6160,111457,"121.478376","31.151565","121.482914297653","31.1496092653813","846","上海市浦东新区华夏西路;中环路入口附近;济阳路出口附近;"
2,460,0,12734,15441,"115.92539","38.472904","115.931151","38.473383","3273","河北省保定市Ｇ４５大广高速"
3,460,0,53750,89674561,"118.84043","31.937084","118.845521","31.934976","2508","江苏省南京市金山路158号"
4,460,1,55180,158866409,"119.99809","29.445627","120.00266","29.443002","1781","浙江省金华市古城西路"
5,460,1,14167,58801,"113.91076","32.28868","113.916366","32.286353","4203","河南省信阳市036县道"
6,460,1,46867,51598335,"113.65495","34.788372","113.661023","34.787257","2073","河南省郑州市农业路27号"
7,460,0,13655,12583,"112.77398","36.351574","112.780321","36.3516","4278","山西省长治市007乡道"
8,460,0,21234,4966,"119.85724","31.800917","119.862036","31.798829","1839","江苏省常州市Hengluo Line"
9,460,0,18273,10531,"118.95372","42.276237","118.959943","42.278206","3875","内蒙古自治区赤峰市三道西街"
10,460,1,46931,54210854,"116.38487","33.909554","116.390417","33.908325","3161","河南省商丘市"
 */

public class PreprocessCellFinalUtf8Csv implements HistoryFilesConfig.Processor {
    private final String dupKeyPatterRegex = "Duplicate entry";
    
    private ContextPoiQuery poiQueryingContext;
    private RepoPoiDao repoPoiDao;
    private RepoGeoDao repoGeoDao;
    private RepoCellStationDao repoCellStationDao;
    
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/cell_final_utf8.csv", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) throws Exception {
                ProfilePoi location = parseLine(line);
                try {
                    repoPoiDao.insertSelective(location);
                } catch (SQLException e) {
                    if (!e.getMessage().trim().contains(dupKeyPatterRegex)) throw e;
                }
                try {
                    repoGeoDao.insertSelective(location);
                }catch (SQLException e) {
                    if (!e.getMessage().trim().contains(dupKeyPatterRegex)) throw e;
                }
                try {
                    repoCellStationDao.insertSelective(location);
                }catch (SQLException e) {
                    if (!e.getMessage().trim().contains(dupKeyPatterRegex)) throw e;
                }
            }
        });
    }
    
    private ProfilePoi parseLine(String line) {
        ProfilePoi location = new ProfilePoi();
        // process line
        String[] fields = line.replace("\"", "").split(",");
        Integer mcc = Integer.parseInt(fields[1].trim());
        Integer mnc = Integer.parseInt(fields[2].trim());
        Integer lac = Integer.parseInt(fields[3].trim());
        Integer cell = Integer.parseInt(fields[4].trim());
        Double raw_longitude = Double.parseDouble(fields[5].trim().replace("\"", ""));
        Double raw_latitude = Double.parseDouble(fields[6].trim().replace("\"", ""));
        Double google_longitude = Double.parseDouble(fields[7].trim().replace("\"", ""));
        Double google_latitude = Double.parseDouble(fields[8].trim().replace("\"", ""));
        
        Integer cellStationAccuracy;
        try {
            cellStationAccuracy = Integer.parseInt(fields[9].trim().replace("\"", ""));
        } catch (Exception e) {
            // -1 means undefined....
            cellStationAccuracy = -1;
        }
        
        // parse location name add code;
        // fill cell_location
        location.setMcc(mcc);
        location.setMnc(mnc);
        location.setLac(lac);
        location.setCell(cell);
        location.setCellStationAccuracy(cellStationAccuracy);
        // file geo_location
        location.setOriginGeoType(GeoCoordinateType.Raw_Coordinate);
        location.setOriginLongitude(raw_longitude);
        location.setOriginLatitude(raw_latitude);
        location.setAdjustGeoType(GeoCoordinateType.Google_Coordinate);
        location.setAdjustLongitude(google_longitude);
        location.setAdjustLatitude(google_latitude);
        // config poi
        String rawAddress = fields[10].trim();
        ProfileCity profile = poiQueryingContext.resolveToCityProfile(rawAddress);
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
	public void setPoiQueryingContext(ContextPoiQuery poiQueryingContext) {
        this.poiQueryingContext = poiQueryingContext;
    }
}
