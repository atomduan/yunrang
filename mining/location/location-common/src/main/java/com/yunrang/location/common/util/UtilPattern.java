package com.yunrang.location.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class UtilPattern {
	public static final String REGEX_CELL_INFO="\"\"cid\"\":\"\"\\d+\"\",\"\"lac\"\":\"\"\\d+\"\"";
	public static final String REGEX_NETWORK_OPERATOR = "\"\"network_operator\"\":\"\"\\d{5}\"\"";
	public static final String REGEX_IP = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
	public static final String REGEX_SESSION_ID = "[A-Z|\\d]{32}";
	public static final String REGEX_APP_ID = "[a-z|\\d]{24}";
	public static final String REGEX_REQUEST_TIME = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}";
    
	private static final Map<String, Pattern> patternMap = new HashMap<String, Pattern>();

	private static Pattern getPattern(String regex) {
		if (patternMap.size()>10000) {
			patternMap.clear();
		}
		if (!patternMap.containsKey(regex)) {
			patternMap.put(regex, Pattern.compile(regex));
		}
		return patternMap.get(regex);
	}

	public static String extractFirst(String line, String regex) {
		return StringUtils.trimToNull(extractFirst(line, getPattern(regex)));
	}
	
	public static String extractFirst(String line, Pattern pattern) {
		line = StringUtils.trimToNull(line);
		if (!StringUtils.isBlank(line)) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				return matcher.group();
			}
		}
		return null;
	}
}
