package com.yunrang.location.integration.support.file.history.data_201306;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;


/**
"0","18179","28893","4372","460","内蒙古自治区呼伦贝尔市111国道","124.41393","48.47568","124.421113","48.477917"
"0","10053","65481","3312","460","广东省惠州市215县道","114.67727","23.505928","114.681883","23.503247"
"0","22717","21807","1117","460","浙江省杭州市石大快速路","120.24589","30.348036","120.25038","30.345596"
"0","21625","21893","2750","460","山东省济宁市火炬路69号","116.624016","35.43338","116.629316","35.432956"
"1","55180","158866207","2179","460","浙江省金华市体育场东路","119.89543","29.445566","119.899931","29.442805"
"0","9273","8658","2243","460","广东省惠州市口岸路5号","114.39865","23.061062","114.403623","23.058545"
"0","29571","46292","1346","460","湖南省株洲市018乡道","113.06273","27.793695","113.068518","27.790549"
"0","16471","46852","3969","460","辽宁省抚顺市创新四路3号","123.69234","41.836323","123.698214","41.838517"
"1","29724","13272","1042","460","湖南省长沙市麻园路","112.943504","28.142689","112.948938","28.139184"
"1","47075","58217711","2805","460","河南省南阳市人民路","111.484146","33.136547","111.490202","33.134795"
 
 */

public class PreprocessTopwangUpdate201305 implements HistoryFilesConfig.Processor {
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/topwang_update_201305", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) {
              //Ignore, this information has been processed in other place
            }
        });
    }
}
