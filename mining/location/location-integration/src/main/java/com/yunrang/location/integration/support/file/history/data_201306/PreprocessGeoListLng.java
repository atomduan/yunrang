package com.yunrang.location.integration.support.file.history.data_201306;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;

/**
"74.921544;39.834579;653024;乌恰县",
"75.258638;38.632494;653022;阿克陶县",
"75.754898;39.409741;653121;疏附县",
"75.843222;37.019583;653131;塔什库尔干塔吉克自治县",
"75.992973;39.470627;653100;喀什地区",
"76.014343;39.513111;653101;喀什市",
"76.137564;39.750346;653000;克孜勒苏柯尔克孜自治州",
"76.368709;38.800015;653123;英吉沙县",
"76.36999;39.187645;653122;疏勒县",
"76.867154;40.13123;653001;阿图什市",
 */

public class PreprocessGeoListLng implements HistoryFilesConfig.Processor {
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/geo_list_lng", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) {
                // ignore....we have get enough gps recordes
            }
        });
    }
}
