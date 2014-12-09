package com.yunrang.hadoop.app.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class TokenUtil {
	public static List<String> getRegions(String startHex, String endHex, int numRegions) throws Exception {
		String start = startHex;
		String end = endHex;
		// init new value
		if (startHex.length() == 0) {
			start = "0";
		}		
		if (endHex.length() == 0) {
			end = "ffffffffffffffff";
		}
		List<String> result = new ArrayList<String>();
		List<BigInteger> biglist = getRegions(new BigInteger(start, 16), new BigInteger(end, 16), numRegions);
		for(BigInteger big : biglist) {
			String hexstring = String.format("%016x", big);
			result.add(hexstring);
		}
		// reset old value
		if (startHex.length() == 0) {
			result.set(0, startHex);
		}		
		if (endHex.length() == 0) {
			result.set(result.size() -1, endHex);
		}		
		return result;
	}

	public static List<BigInteger> getRegions(BigInteger start, BigInteger end, int numRegions) throws Exception {
		if (start.compareTo(end) > 0) {
			throw new Exception("" + start + " > " + end);
		}
		List<BigInteger> result = new ArrayList<BigInteger>();
		BigInteger range = end.subtract(start);
		BigInteger regionIncrement = range.divide(BigInteger.valueOf(numRegions));
		for (int i = 0; i < numRegions; i++) {
			BigInteger value = start.add(regionIncrement.multiply(BigInteger.valueOf(i)));
			result.add(value);
		}
		result.add(end);
		return result;
	}
}