package com.yunrang.location.tasklet.mapreduce.libra.parser;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.yunrang.location.common.bean.ProfileCellStation;
import com.yunrang.location.common.util.UtilPattern;
/**
 * ""cid"":""32640"",""lac"":""33557""
 * ""network_operator"":""46000"" 
 */
public class LibraLogRecordParser {
    private static final Pattern PATTERN_CELL_INFO=Pattern.compile(UtilPattern.REGEX_CELL_INFO);
    private static final Pattern PATTERN_NETWORK_OPERATOR = Pattern.compile(UtilPattern.REGEX_NETWORK_OPERATOR);
    private static final Pattern PATTERN_IP = Pattern.compile(UtilPattern.REGEX_IP);
    private static final Pattern PATTERN_SESSION_ID = Pattern.compile(UtilPattern.REGEX_SESSION_ID);
    private static final Pattern PATTERN_APP_ID = Pattern.compile(UtilPattern.REGEX_APP_ID);
    private static final Pattern PATTERN_REQUEST_TIME = Pattern.compile(UtilPattern.REGEX_REQUEST_TIME);
    
    public static String extractRequestTime(String line) {
    	try {
    		return StringUtils.trimToNull(UtilPattern.extractFirst(line, PATTERN_REQUEST_TIME));
    	} catch (Exception e) {
    		return null;
    	}
    }
    
    public static String extractAppId(String line) {
    	try {
    		return StringUtils.trimToNull(UtilPattern.extractFirst(line, PATTERN_APP_ID));
    	} catch (Exception e) {
    		return null;
    	}
    }
    
    public static String extractSessionId(String line) {
    	try {
    		return StringUtils.trimToNull(UtilPattern.extractFirst(line, PATTERN_SESSION_ID));
    	} catch (Exception e) {
    		return null;
    	}
    }

    public static String extractGatewayIp(String line) {
    	try {
    		//潜规则gateway ip 总是在日志中第一个出现的ip
    		return StringUtils.trimToNull(UtilPattern.extractFirst(line, PATTERN_IP));
    	} catch (Exception e) {
    		return null;
    	}
    }
    
    public static ProfileCellStation parseProfileCellStation(String line) {
    	ProfileCellStation profileCellStation = new ProfileCellStation();
    	try {
    		String[] cellInfoParams= UtilPattern.extractFirst(line, PATTERN_CELL_INFO).replace("\"", "").split(",");
        	String[] networkOperatorParams = UtilPattern.extractFirst(line, PATTERN_NETWORK_OPERATOR).replace("\"", "").split(":");
            String lac = StringUtils.trimToNull(cellInfoParams[1].split(":")[1]);
            String cid = StringUtils.trimToNull(cellInfoParams[0].split(":")[1]);
            String opt = StringUtils.trimToNull(networkOperatorParams[1]);
            String mcc = opt.substring(0,3);
            String mnc = opt.substring(3);
            profileCellStation.setCid(cid);
            profileCellStation.setLac(lac);
            profileCellStation.setMcc(mcc);
            profileCellStation.setMnc(mnc);
            profileCellStation.setOpt(opt);
    	} catch (Exception ignore) {}
        return profileCellStation;
    }
}
