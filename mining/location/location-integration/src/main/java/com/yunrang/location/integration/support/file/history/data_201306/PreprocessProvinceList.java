package com.yunrang.location.integration.support.file.history.data_201306;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;


/**
"11;北京市",
"12;天津市",
"13;河北省",
"14;山西省",
"15;内蒙古自治区",
"21;辽宁省",
"22;吉林省",
"23;黑龙江省",
"31;上海市",
"32;江苏省",
 */

public class PreprocessProvinceList implements HistoryFilesConfig.Processor {
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/province_list", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) {
                //Ignore, this information has been processed in other place
            }
        });
    }
}
