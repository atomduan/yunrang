package com.yunrang.location.tasklet.mapreduce.libra.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LibralogHdfsPathFilterStatisticAdvlog implements PathFilter {
	private static Logger LOG = LoggerFactory.getLogger(LibralogHdfsPathFilterStatisticAdvlog.class);
	
	private static Pattern datePattern = Pattern.compile("201[0-9]{1}/[0-9]{2}/[0-9]{2}");
	private static String header = "production/ads/log/libra";
    private static String preffix = "statisticadvlog.statistic_advlog";
    private static String suffix = ".csv";
    private static String[] includeIPs = new String[]{"10.21.114.11", "10.21.114.12", "10.21.114.22", "10.21.114.23", "10.21.114.24", "10.21.115.10",};
    
	private FilterDate beginDate;
    private FilterDate endDate;
    
    public LibralogHdfsPathFilterStatisticAdvlog(String beginDateStr, String endDateStr) {
    	this.beginDate = this.parseDate(beginDateStr);
    	this.endDate = this.parseDate(endDateStr);
    }
    
    /**
     * urlStr sample : production/ads/log/libra/10.21.114.22/2013/05/31/16/statisticadvlog.statistic_advlog_2013053116.csv
     */
    public boolean accept(Path inputFilePath) {
        String urlStr = inputFilePath.toUri().getPath().trim();
        if (ipCheckPass(urlStr)) {
            if (urlStr.contains(header)) {
            	String filePathName = inputFilePath.getName();
            	if (filePathName.startsWith(preffix) && filePathName.endsWith(suffix)) {
            		if (dateCheckPass(urlStr)) {
            			return true;
            		}
                }
            }
        }
        return false;
    }
 
    private FilterDate parseDate(String line) {
    	Matcher m = datePattern.matcher(line);
    	String dateStr = "";
    	if (m.find()) {
    		dateStr = m.group();
    		String[] params = dateStr.trim().split("/");
    		FilterDate d = new FilterDate();
    		d.year = Integer.parseInt(params[0]);
    		d.month = Integer.parseInt(params[1]);
    		d.day = Integer.parseInt(params[2]);
    		return d;
    	}
    	return null;
    }
    
    private boolean dateCheckPass(String urlStr) {
    	try {
    		FilterDate urlDate = this.parseDate(urlStr);
    		if (urlDate.laterThan(this.beginDate) == true) {
    			if (urlDate.laterThan(this.endDate) == false) {
    				return true;
    			}
    		}
    	} catch (Exception e) {
    		LOG.warn("fail to parse	beginDate:["+beginDate+"];endDate:["+endDate+"];urlStr:["+urlStr+"]", e);
    	}
    	return false;
    }
    
    private boolean ipCheckPass(String urlStr) {
        for (String ip : includeIPs) {
            if (urlStr.contains(ip)) {
                return true;
            }
        }
        return false;
    }
    
    private class FilterDate {
    	private Integer year;
    	private Integer month;
    	private Integer day;
    	public String toString() {
    		return ToStringBuilder.reflectionToString(this);
    	}
    	private Integer getMeasure() {
    		return year*365+month*31+day;
    	}
		public boolean laterThan(FilterDate o) {
			if (this.getMeasure() >= o.getMeasure()) {
				return true;
			} else {
				return false;
			}
		}
    }
}