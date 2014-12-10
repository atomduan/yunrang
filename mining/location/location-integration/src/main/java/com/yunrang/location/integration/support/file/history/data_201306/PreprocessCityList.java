package com.yunrang.location.integration.support.file.history.data_201306;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;


/**
110000 北京市
110100 市辖区
110101 东城区
110102 西城区
110105 朝阳区
110106 丰台区
110107 石景山区
110108 海淀区
110109 门头沟区
110111 房山区
 */

public class PreprocessCityList implements HistoryFilesConfig.Processor {
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/city_list", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) {
                //ignore...., we have set this information in config file
            }
        });
    }
}
