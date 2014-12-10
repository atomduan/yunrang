package com.yunrang.location.integration.support.file.history.data_201306;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;


/**
"1100;北京市",
"1101;市辖区",
"1102;县",
"1200;天津市",
"1201;市辖区",
"1202;县",
"1300;河北省",
"1301;石家庄市",
"1302;唐山市",
"1303;秦皇岛市",
 */
public class PreprocessRegionCode implements HistoryFilesConfig.Processor {
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/region_code", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) {
              //Ignore, this information has been processed in other place
            }
        });
    }
}
