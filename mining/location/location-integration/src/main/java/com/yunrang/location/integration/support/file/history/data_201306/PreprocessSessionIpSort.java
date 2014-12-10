package com.yunrang.location.integration.support.file.history.data_201306;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;

/**
000005EB4BCE5448E5A924C11B5C8365,175.167.136.244,0
000005EB4BCE5448E5A924C11B5C8365,175.167.136.244,2
000007A6A6F6B3863E1E863EC063CFD0,171.13.20.87,0
000007A6A6F6B3863E1E863EC063CFD0,171.13.20.87,2
00000EC967467ADD8AB6E33ABFB1F315,183.19.223.239,0
00000EC967467ADD8AB6E33ABFB1F315,183.19.223.239,2
0000106566BC8BA88ECD24251C2B484D,218.205.237.10,0
0000129E7613FAC82A5EC2B23C5C081A,211.138.45.42,0
00001539DFD666F4FEFD6B1DEA45BF6B,182.118.188.50,0
00001539DFD666F4FEFD6B1DEA45BF6B,182.118.188.50,2
 */

public class PreprocessSessionIpSort implements HistoryFilesConfig.Processor {
    private final Logger Log = LoggerFactory.getLogger(PreprocessSessionIpSort.class);
    private Map<String, String> sessionRecords = new HashMap<String, String>();
    private Long totalDiffCount = 0L;
    private Long totalCount = 0L;
    
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doSingleThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/session_ip_sort", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) throws Exception {
                doProcessLine(line);
                doMockInject();
            }
            
            private Boolean injected = false;
            private void doMockInject() throws Exception {
                String line = "00001539DFD666F4FEFD6B1DEA45BF6B,182.118.188.51,0";
                if (false == injected) {
                    doProcessLine(line); 
                }
                injected = true;
            }
            
            private void doProcessLine(String line) throws Exception {
                String[] fields = line.trim().split(",");
                String sessionId = fields[0];
                String ipAndType = fields[1]+","+fields[2];
                if (sessionRecords.containsKey(sessionId)) {
                    sessionRecords.put(sessionId, ipAndType+"#"+sessionRecords.get(sessionId));
                } else {
                    sessionRecords.put(sessionId, ipAndType);
                }
            }
            
            public void cleanup() throws Exception {
                Set<String> ipTestSet = new HashSet<String>();
                String recordStr = null;
                for (String sessionId : sessionRecords.keySet()) {
                    try {
                        totalCount++;
                        recordStr = sessionRecords.get(sessionId);
                        String [] records = recordStr.split("#");
                        for (String r : records) {
                            String[] fields = r.split(",");
                            String ip = fields[0];
                            if (!ipTestSet.contains(ip)) {
                                ipTestSet.add(ip);
                            }
                        }
                        if (ipTestSet.size()>1) {
                            totalDiffCount++;
                        }
                    } catch (Exception e) {
                        Log.info(recordStr);
                        throw e;
                    } finally {
                        ipTestSet.clear();
                    }
                }
                Log.info("totalCount:"+totalCount+" / "+"totalDiffCount:"+totalDiffCount);
            }
        });
    }
}
