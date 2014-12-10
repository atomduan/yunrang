package com.yunrang.location.common.util;

public class UtilIpConverter {
    public static Long toLongValue(String ip) {
        long lp = 0;
        String[] ips = ip.split("\\.");
        for (int i = 0; i < ips.length; i++) {
            lp += Long.parseLong(ips[ips.length - i - 1]) << i * 8;
        }
        return lp;
    }
    
    public static String toStringValue(Long ipNum) {
        Long a = ipNum % 256L;
        Long b = (ipNum % 65536L - a) / 256L;
        Long c = (ipNum % 16777216L - a - b) / 65536L;
        Long d = ipNum / 16777216L;
        return d+"."+c+"."+b+"."+a;
    }
}