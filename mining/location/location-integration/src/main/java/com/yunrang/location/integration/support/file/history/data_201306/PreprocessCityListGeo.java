package com.yunrang.location.integration.support.file.history.data_201306;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;


/**
110000;北京市;39.929986;116.395645
110100;市辖区;;
110101;东城区;39.938574;116.421885
110102;西城区;39.93428;116.37319
110105;朝阳区;39.958953;116.521695
110106;丰台区;39.841938;116.25837
110107;石景山区;39.938867;116.184556
110108;海淀区;40.033162;116.239678
110109;门头沟区;40.000893;115.795795
110111;房山区;39.726753;115.862836
 */

public class PreprocessCityListGeo implements HistoryFilesConfig.Processor {
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/city_list_geo", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) {
                // ignore....ignore....we have get enough gps recordes
            }
        });
    }
}
