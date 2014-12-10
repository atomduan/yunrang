package com.yunrang.location.service.report;

import java.util.List;

import com.yunrang.location.service.support.ip.LocationSinaIpRange;


public class ReportSinaIpResolve {
	private LocationSinaIpRange locationSinaIpRange;
    
    public ReportSinaIpResolve() throws Exception {
        this.locationSinaIpRange = new LocationSinaIpRange();
    }
    
    private void printIpArray(String[] ipArray) throws Exception {
    	for (String ip : ipArray) {
    		List<String> list = locationSinaIpRange.getProvinceAndCity(ip);
    		String result = "";
            if (list != null) {
                for (String s : list) {
                    result += s+"#";
                }
            } else {
            	result = "未能通过Ip库命中此Ip";
            }
            System.out.println(ip+"\t:\t"+result);
        }
    }
    
    public static void __main(String[] args) throws Exception {
    	ReportSinaIpResolve sinaIpResolveReport = new ReportSinaIpResolve();
    	String[] ipArray = new String[] {
        		"210.14.10.56",
        };
    	sinaIpResolveReport.printIpArray(ipArray);
    }
}
