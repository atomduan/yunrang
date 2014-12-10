package com.yunrang.location.integration.support.file.history.data_201306;

import java.sql.SQLException;

import com.yunrang.location.common.bean.ProfilePoi;
import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.common.enums.GeoCoordinateType;
import com.yunrang.location.datamgr.mysql.dao.RepoCellStationDao;
import com.yunrang.location.datamgr.mysql.dao.RepoGeoDao;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;

/**
46000,10003,103,116.4206122,39.8599028
46000,10003,21952,113.2370216,23.2312751
46000,10003,44399,113.2286429,23.2312134
46000,10003,45251,113.245278,23.2043349
46000,10003,45252,113.2472663,23.2055456
46000,10003,45253,113.2383149,23.2080151
46000,10003,45307,113.2472663,23.2055456
46000,10003,45309,113.23915,23.2048695
46000,10003,45531,113.2326134,23.2013666
46000,10004,26672,113.325823,23.1901368
 */

public class Preprocess2013CellIdWeiboFin implements HistoryFilesConfig.Processor {
    private final String dupKeyPatterRegex = "Duplicate entry";
    
    private RepoGeoDao repoGeoDao;
    private RepoCellStationDao repoCellStationDao;
    
	public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/2013_cell_id_weibo_fin", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) throws Exception {
                ProfilePoi location = parseLine(line);
                if (null != location) {
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
            }
        });
    }
    
    private ProfilePoi parseLine(String line) {
        ProfilePoi location = new ProfilePoi();
        // process line
        String[] fields = line.trim().split(",");
        String mccAndMnc = fields[0];
        Integer mcc;
        Integer mnc;
        if (mccAndMnc.startsWith("460")) {
            mcc = 460;
            mnc = Integer.parseInt(mccAndMnc.replace("460", ""));
            Integer lac = Integer.parseInt(fields[1]);
            Integer cell = Integer.parseInt(fields[2]);
            GeoCoordinateType originGeoType = GeoCoordinateType.Sina_Coordinate;
            Double originLongitude = Double.parseDouble(fields[3]);
            Double originLatitude = Double.parseDouble(fields[4]);
            location.setMcc(mcc);
            location.setMnc(mnc);
            location.setLac(lac);
            location.setCell(cell);
            location.setOriginGeoType(originGeoType);
            location.setOriginLongitude(originLongitude);
            location.setOriginLatitude(originLatitude);
            return location;
        }
        return null;
    }
    
    public void setRepoGeoDao(RepoGeoDao repoGeoDao) {
		this.repoGeoDao = repoGeoDao;
	}
	public void setRepoCellStationDao(RepoCellStationDao repoCellStationDao) {
		this.repoCellStationDao = repoCellStationDao;
	}
}
