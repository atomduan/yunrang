package com.yunrang.location.integration.support.file.history.data_201307;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;

/**
 * F6A0F97D81227AEB880147BE289E8228###117.136.44.149###519b08d8498e66e39852dc34        ,519b097f498e66e39852dc48        ,519b09b2498e66e39852dc49        ,,5174ff68498e8c0e5de101ac        ,1370053883816,0,0,2013-06-01 10:31:23.816,5170c2f3498e8c0e5de1013a        ,y5jEFaw4eq,,F6A0F97D81227AEB880147BE289E8228,866796011093669,UNICOM,"Mozilla/5.0 (Linux; U; Android 4.0.4; zh-cn; Lenovo A530 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 (Mobile; Suizong_3.0)",Android,Lenovo A530,117.136.44.149,,Suizong_3.0,URL,北京,nil                                                             ,B9F1F619B8843F2BCC1D05D5C39CAEF6,,1,0.00000,0.00000,"{""device_id"":""866796011093669"",""network_country_iso"":""cn"",""network_operator"":""46001"",""network_type"":""10"",""sim_country_iso"":""cn,cn"",""sim_operator"":""46001"",""sim_serial_number"":""89860113876014846214"",""sim_state"":""5"",""subscriber_id"":""460014702618431"",""is_network_roaming"":""false"",""cell_type"":""gsm"",""cell_location"":{""cid"":""55670806"",""lac"":""46977"",""base_station_id"":null,""base_station_latitude"":null,""base_station_longitude"":null,""network_id"":null,""system_id"":null},""location"":{""accuracy"":""0"",""altitude"":""0"",""bearing"":""0"",""extras"":null,""latitude"":""0"",""longitude"":""0"",""provider"":""gps"",""speed"":""0"",""time"":""0""}}",PLARFORM,3.0,SUIZONG,3.0,,,,_3G
 *
 */
public class PreprocessBugIpLocation implements HistoryFilesConfig.Processor {
    private final Logger Log = LoggerFactory.getLogger(PreprocessBugIpLocation.class);
    private final Pattern IP_PATTERN = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+"); 
    private final String[] mongoServerIPs = new String[] { "118.244.212.53",
            "118.244.212.58", "118.244.212.59", "118.244.212.54",
            "118.244.212.55", "118.244.212.56", "118.244.212.60",
            "118.244.212.61", "118.244.212.62", "115.182.31.2",
            "115.182.31.3", "115.182.31.4", "115.182.31.5", "115.182.31.6",
            "115.182.31.7", "115.182.31.8", "115.182.31.9",
            "115.182.31.10", "115.182.31.11", "115.182.31.12",
            "115.182.31.13", "110.249.162.234" };
    private Integer totalCount = 0;
    private Integer mongoCount = 0;
    private Integer equalCount = 0;
    private Integer undefineCount = 0;

    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doSingleThreadProcess (HistoryFilesConfig.yrDataPath_201307+"/bug_ip_location", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) throws Exception {
                totalCount++;
                String[] fields = line.split("###");
                String session = fields[0];
                String ipFromGuest = fields[1];
                String rawRecord = fields[2];
                Matcher m = IP_PATTERN.matcher(rawRecord);
                String ipFromLog = m.find() ? m.group() : "";
                if (!ipFromGuest.equals(ipFromLog)) {
                    if (!isMongoServerIP(ipFromLog)) {
                        Log.info(session+"\t"+ipFromGuest+"\t"+ipFromLog+"\t"+"-");
                        undefineCount++;
                    } else {
                        Log.info(session+"\t"+ipFromGuest+"\t"+ipFromLog+"\t"+"M");
                        mongoCount++;
                    }
                } else {
                    Log.info(session+"\t"+ipFromGuest+"\t"+ipFromLog+"\t"+"=");
                    equalCount++;
                }
            }
            
            public void cleanup() {
                Log.info("totalCount:["+totalCount+"] ; mongoCount ["+mongoCount+"] ; equalCount ["+equalCount+"] ; undefineCount ["+undefineCount+"] ");
            }
        });
    }
    
    private Boolean isMongoServerIP(String ipStr) {
        for (String p : mongoServerIPs) {
            if (p.equals(ipStr)) {
                return true;
            }
        }
        return false;
    }
}
