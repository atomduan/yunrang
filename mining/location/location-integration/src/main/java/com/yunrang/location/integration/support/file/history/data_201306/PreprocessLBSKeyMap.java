package com.yunrang.location.integration.support.file.history.data_201306;

import java.util.HashMap;
import java.util.Map;

import com.yunrang.location.common.config.PoiConfig;
import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;
/**
Province,City,Detail,keyOfIDCard,keyOfWingSoft,KeyOfGeoIP
河北,,河北省,130000,河北省,Hebei
河北,石家庄,石家庄市,130100,石家庄市,Shijiazhuang
河北,唐山,唐山市,130200,唐山市,Tangshan
河北,秦皇岛,秦皇岛市,130300,秦皇岛市,Qinhuangdao
河北,邯郸,邯郸市,130400,邯郸市,Handan
河北,邢台,邢台市,130500,邢台市,Xingtai
河北,保定,保定市,130600,保定市,Baoding
河北,张家口,张家口市,130700,张家口市,Zhangjiakou
河北,承德,承德市,130800,承德市,Chengde
 */

public class PreprocessLBSKeyMap implements HistoryFilesConfig.Processor {
    
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doSingleThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/LBS_key_map", new ContextFileProcess.LineProcessor(){
            private Map<String, String> cityPinYin = new HashMap<String, String>();
            public void processLine(String line) {
                String[] fields = line.split(",");
                String cityCode = fields[3].substring(0,4);
                cityPinYin.put(cityCode, fields[5]);
            }
            
            public void cleanup() throws Exception {
                for (String s : PoiConfig.PROVINCE_CODE) {
                    String[] fields = s.split(";");
                    String cityCode = fields[0]+"00";
                    String r = "";
                    for (int i=0; i<fields.length; i++) {
                        if (i == 0) {
                            r += fields[i];
                        } else if (i == 1){
                            r += "#" + fields[i];
                        } else {
                            r += ";" + fields[i];
                        }
                    }
                    System.out.println("\""+r+"#"+cityPinYin.get(cityCode)+"\",");
                }
            }
        });
    }
}
