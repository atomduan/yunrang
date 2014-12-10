package com.yunrang.location.integration.support.file.history.data_201306;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;

/**
460 0 6160 111457 121.478376 31.151565
460 0 12734 15441 115.92539 38.472904
460 0 53750 89674561 118.84043 31.937084
460 1 55180 158866409 119.99809 29.445627
460 1 14167 58801 113.91076 32.28868
460 1 46867 51598335 113.65495 34.788372
460 0 13655 12583 112.77398 36.351574
460 0 21234 4966 119.85724 31.800917
460 0 18273 10531 118.95372 42.276237
460 1 46931 54210854 116.38487 33.909554
 */

public class PreprocessCellLightTxt implements HistoryFilesConfig.Processor {
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/cell_light.txt", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) {
                //Ignore, We don,t need to process this file, 20130625
            }
        });
    }
}
