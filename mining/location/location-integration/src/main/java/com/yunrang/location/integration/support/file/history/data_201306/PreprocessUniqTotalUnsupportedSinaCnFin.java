package com.yunrang.location.integration.support.file.history.data_201306;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;


/**
46000,1000,2000
46000,10002,102
46000,10003,12910
46000,10003,21951
46000,10003,45308
46000,10003,51504
46000,10003,52416
46000,10003,59631
46000,10004,21502
46000,10004,23048
 */

public class PreprocessUniqTotalUnsupportedSinaCnFin implements HistoryFilesConfig.Processor {
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/uniq_total_unsupported_sina_cn_fin", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) {
                //ignore....
            }
        });
    }
}
