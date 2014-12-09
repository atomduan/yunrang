package com.yunrang.hadoop.app.common.config;

import java.util.HashMap;
import java.util.Map;

public enum HBaseWeiboColumns {
	 // TimeStamp for cols (temp)
	TS("TS"),
	ACTION("ACTION"),
	
	id("id"),
	UID("1"),
	CONTENT("2"),
	URL("3"),
	TIME("4"),
	IP("5"),
	ZONE("6"),
	ID("7"),
	SOURCE("8"),
	USER_TYPE("9"),
	digit_attr("10"),
	digit_source("11"),
	digit_uid("12"),
	digit_time("13"),
	AUDIT("14"),
	FILTER("15"),
	TAG("16"),
	DATA("17"),
	WORDS("18"),
	LEVEL("19"),
	STATUS("20"),
	VERIFIED("23"),
	VERIFIEDTYPE("24"),
	PRIVACY("25"),
	DUP("26"),
	QI("27"),
	QIHD("28"),
	IDXCONTENT("30"),
	DUP_CONT("31"),
	DUP_URL("32"),
	IDXTEXT("33"),
	USERTEXT("34"),
	FWNUM("35"),
	CMTNUM("36"),
	VALIDFWNM("37"),
	UNVIFWNM("38"),
	IDXLEN("39"),
	TOPIC_WORDS("40"),
	NON_TOPIC_WORDS("41"),
	DEFLEN("42"),
	
	// supply
	LURL("29"),
	RTMID("44"),
	ROOTMID("45"),
	ROOTUID("46"),
	ROOTTEXT("47"),
	PIC("48"),
	GRIDCODE("49"),
	
	// new code from the beginning of 60
	LIKENUM("60"),
	WHITELIKENUM("61"),
	BLACKLIKENUM("62"),
	CONT_SIGN("63"),
	UNRNUM("64"),
	IDENTIFY_TYPE("65")
	;
	
	private static final Map<String, String> CODE_TO_NAME = new HashMap<String, String>();
	private static final Map<String, String> NAME_TO_CODE = new HashMap<String, String>();
	
	static {
		for (HBaseWeiboColumns e : HBaseWeiboColumns.values()) {
			String code = e.getCode();
			String name = e.getName();
			CODE_TO_NAME.put(code, name);
			NAME_TO_CODE.put(name, code);
		}
	}
	
	public static boolean isValidFieldsName(String name) {
		return NAME_TO_CODE.containsKey(name);
	}
	
	String code;
	HBaseWeiboColumns(String code) {	
		this.code = code;
	}
	
	public String getName() {
		return this.toString();
	}
	
	public String getCode() {
		return code;
	}
	
	public boolean codeEquals(String code) {
		return this.getCode().equals(code);
	}
	
	public static String getName(String code) {
		return CODE_TO_NAME.get(code);
	}
	
	public static String getCode(String name) {
		return NAME_TO_CODE.get(name);
	}
}
