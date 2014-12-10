package com.yunrang.location.integration.support.file;

public class HistoryFilesConfig {
    public static String yrDataPath_201306 = "/var/yr/ads_location/history/data_201306";
    public static String yrDataPath_201307 = "/var/yr/ads_location/history/data_201307";
    public static String yrDataPath_201308 = "/var/yr/ads_location/history/data_201308";
    public static String yrDataPath_201309 = "/var/yr/ads_location/history/data_201309";
    public static interface Processor {
    	public void doProcess() throws Exception;
    }
}
