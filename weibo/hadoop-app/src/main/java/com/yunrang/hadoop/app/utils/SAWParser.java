package com.yunrang.hadoop.app.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Simple Api for Weibo Stream
 * @author juntaoduan
 *
 */
public class SAWParser {
	public static final String RECORDE_SEP = "@";
	public static final String FIELD_SEP = ":";
	public static final String NEW_LINE = "\n";
	private Map<String, String> attrMap;
	private RecordHandler handler;
	
	private SAWParser(RecordHandler handler) {
		this.handler = handler;
		this.attrMap = new HashMap<String, String>();
	}
	
	public void offerLine(String line) {
		line = StringUtils.trimToNull(line);
		if (line != null) {
			if (line.equals(RECORDE_SEP)) {
				try {
					handler.processOneRecord(attrMap);
				} catch (Exception e) {}
				attrMap.clear();
			} else {
				processOneLine(line);
			}
		}
	}
	
	public void flush() {
		handler.processOneRecord(attrMap);
		attrMap.clear();
	}
	
	private void processOneLine(String line) {
		if (line.contains(FIELD_SEP)) {
			String key = StringUtils.trimToNull(line.substring(0, line.indexOf(FIELD_SEP)));
			String value = StringUtils.trimToNull(line.substring(line.indexOf(FIELD_SEP) + 1 , line.length()));
			if (key != null) {
				key = key.replace(RECORDE_SEP, "");
				attrMap.put(key, value);
			}
		}
	}
	
	public static String linageRecorde(Map<String, String> attrMap) {
		String record = SAWParser.RECORDE_SEP;
    	for (String key : attrMap.keySet()) {
    		String value = attrMap.get(key);
    		String line = SAWParser.RECORDE_SEP+key+SAWParser.FIELD_SEP+StringUtils.trimToEmpty(value);
    		record += SAWParser.NEW_LINE + line;
    	}
		return record;
	}
	
	public static Map<String, String> delinageRecorde(String recorde) {
		Map<String, String> r = new HashMap<String, String>();
		String[] params = recorde.split(SAWParser.NEW_LINE);
		for (String line : params) {
			if (line == null) {
				continue;
			}
			line = StringUtils.trimToNull(line);
			if (line != null && line.contains(FIELD_SEP)) {
				String key = StringUtils.trimToNull(line.substring(0, line.indexOf(FIELD_SEP)));
				String value = StringUtils.trimToNull(line.substring(line.indexOf(FIELD_SEP) + 1 , line.length()));
				if (key != null) {
					key = key.replace(RECORDE_SEP, "");
					r.put(key, value);
				}
			}
		}
		return r;
	}
	
	public static interface RecordHandler {
		public void processOneRecord(Map<String, String> attrMap);
	}
	
	public static SAWParser getParser(RecordHandler handler) {
		return new SAWParser(handler);
	}
}
