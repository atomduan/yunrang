package com.yunrang.location.integration.support.file.history.data_201306;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;



/**
00000,0,0
000000,10034,0
000000,10446,10719
000000,1140,0
000000,12657,14536
000000,16657,0
000000,16657,40463
000000,16772,0
000000,22294,10213
000000,28948,0

 */
public class PreprocessUniqTotal implements HistoryFilesConfig.Processor {
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/uniq_total", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) {
                //ignore....
            }
        });
    }
}
