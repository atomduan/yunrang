package com.yunrang.hadoop.app.utils;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONObject;

import com.yunrang.hadoop.app.common.config.HBaseWeiboColumns;

public class WeiboUtil {
	
	public static Integer getIntField(JSONObject record, HBaseWeiboColumns fieldEnum) {
		return parseInt((String) tryGet(record, fieldEnum.getCode()));
	}
	public static String getStringField(JSONObject record, HBaseWeiboColumns fieldEnum) {
		return StringUtils.trimToEmpty((String) tryGet(record, fieldEnum.getCode()));
	}
	public static boolean isCheating(Integer QI) {
		return (QI & 1042) == 1042;
	}
	public static boolean isLowQuality(Integer QI) {
		return ((QI >>> 5) & 0x1) == 0;
	}
	public static boolean isBlackListUser(Integer QI) {
		return ((QI >>> 15) & 0x1) > 0 || ((QI >>> 18) & 0x1) > 0;
	}
	public static boolean isBlackListUser(String uid) {
		return false;
	}
	public static boolean isBlackListSource(Integer QI) {
		return ((QI >>> 13) & 0x1) > 0;
	}
	public static boolean isIllegalEvent(Integer QI) {
		return ((QI >>> 1) & 0x1) > 0;
	}
	public static boolean isImageContained(Integer filter) {
		return (filter & 0x1) > 0;
	}
	public static boolean isForwardedWeibo(Integer filter) {
		return ((filter >>> 2) & 0x1) > 0;
	}
	public static boolean isVedioContained(Integer filter) {
		return ((filter >>> 4) & 0x1) > 0;
	}
	public static boolean isMusicContained(Integer filter) {
		return ((filter >>> 5) & 0x1) > 0;
	}
	public static Integer chineseCharCount(String content) {
		Integer result = 0;
		for (int i=0; i<content.length(); i++) {
			char c = content.charAt(i);
			if ((int)c > 256) {
				result++;
			}
		}
		return result;
	}
	public static String tryGet(JSONObject record, String key) {
		try {
			return record.getString(key);
		} catch (Exception e) {
			return null;
		}
	}
	public static Integer parseInt(String value) {
		try {
			return Integer.parseInt(StringUtils.trimToEmpty(value));
		} catch (Exception e) {
			return 0;
		}
	}
	public static Long parseLong(String value) {
		try {
			return Long.parseLong(StringUtils.trimToEmpty(value));
		} catch (Exception e) {
			return 0L;
		}
	}
	public static void diagnosticate(String filterStr, String QIStr) {
		Integer filter = WeiboUtil.parseInt(filterStr);
		Integer QI = WeiboUtil.parseInt(QIStr);
		if (WeiboUtil.isCheating(QI)) {
			System.out.println("cheating");
		}
		if (WeiboUtil.isBlackListUser(QI)) {
			System.out.println("black user");
		}
		if (WeiboUtil.isLowQuality(QI) == true) {
			System.out.println("low quality");
		}
		if (WeiboUtil.isForwardedWeibo(filter) == true) {
			System.out.println("forward weibo");
		}
		if (WeiboUtil.isImageContained(filter)) {
			System.out.println("contain img");
		}
		if (WeiboUtil.isMusicContained(filter)) {
			System.out.println("contain music");
		}
		if (WeiboUtil.isVedioContained(filter)) {
			System.out.println("contain vedio");
		}
	}
}
