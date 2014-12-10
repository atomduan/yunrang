package com.yunrang.location.integration.support.file.history.data_201306;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;


/**
"4.974366;112.660302;460322;南沙群岛",
"16.497085;111.673087;460321;西沙群岛",
"18.257776;109.522771;460200;三亚市",
"18.575985;109.948661;469028;陵水黎族自治县",
"18.597592;109.656113;469029;保亭黎族苗族自治县",
"18.658614;109.062698;469027;乐东黎族自治县",
"18.831306;109.51775;469001;五指山市",
"18.839886;110.292505;469006;万宁市",
"18.998161;108.85101;469007;东方市",
"19.039771;109.861849;469030;琼中黎族苗族自治县", 
 */

public class PreprocessGeoListLat implements HistoryFilesConfig.Processor {
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/geo_list_lat", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) {
                //ignore....we have get enough gps recordes
            }
        });
    }
}
