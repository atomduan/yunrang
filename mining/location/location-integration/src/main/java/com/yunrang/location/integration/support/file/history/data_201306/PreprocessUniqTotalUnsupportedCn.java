package com.yunrang.location.integration.support.file.history.data_201306;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;


/**
46000,1000,2000
46000,10002,102
46000,10003,103
46000,10003,12910
46000,10003,21951
46000,10003,21952
46000,10003,44399
46000,10003,45251
46000,10003,45252
46000,10003,45253

 */
public class PreprocessUniqTotalUnsupportedCn implements HistoryFilesConfig.Processor {
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/uniq_total_unsupported_cn", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) {
                //ignore....
            }
        });
    }
}
