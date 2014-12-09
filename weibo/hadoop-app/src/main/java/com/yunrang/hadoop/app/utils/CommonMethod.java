package com.yunrang.hadoop.app.utils;


public class CommonMethod {
	public static boolean isNormal(String mid) {		
		if (mid.length() != 16) 
			return false;
		
		if (mid.compareTo("3342818919841793") < 0 || mid.compareTo("4500000000000000") > 0) 
			return false;
	
		return true;
	}	
}
