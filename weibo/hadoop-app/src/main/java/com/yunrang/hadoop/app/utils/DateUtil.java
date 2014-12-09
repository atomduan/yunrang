package com.yunrang.hadoop.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateUtil {
	private DateUtil() {}
	public static SimpleDateFormat findFormat(String date) {
		return findFormat(findIntFormat(date));
	}
	/**
	 * find a format by code
	 * @param intFormat
	 */
	public static SimpleDateFormat findFormat(int intFormat) {
		String strFormat = "yyyy-MM-dd HH:mm:ss";
		switch (intFormat) {
		case 1: // '\year'
			strFormat = "yyyy";
			break;
		case 2: // '\month'
			strFormat = "yyyy-MM";
			break;
		case 3: // '\day'
			strFormat = "yyyy-MM-dd";
			break;
		case 4: // '\hour'
			strFormat = "yyyy-MM-dd HH";
			break;
		case 5: // '\minute'
			strFormat = "yyyy-MM-dd HH:mm";
			break;
		case 6: // '\second'
			strFormat = "yyyy-MM-dd HH:mm:ss";
			break;
		default:
			strFormat = "yyyy-MM-dd HH:mm:ss";
			break;
		}
		return new SimpleDateFormat(strFormat);
	}

	public static String addDate(String dateStr, int intFormat, int addNum) throws Exception {
		Calendar calendar = null;
		calendar = GregorianCalendar.getInstance();
		calendar.setTime(findFormat(intFormat).parse(dateStr));
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		if (intFormat == 1) {
			calendar.add(Calendar.YEAR, addNum);
		} else if (intFormat == 2) {
			calendar.add(Calendar.MONTH, addNum);
		} else if (intFormat == 3) {
			calendar.add(Calendar.DAY_OF_MONTH, addNum);
		} else if (intFormat == 4) {
			calendar.add(Calendar.HOUR_OF_DAY, addNum);
		} else {
			calendar.add(Calendar.DATE, addNum);
		}
		return findFormat(intFormat).format(calendar.getTime());
	}
	
	public static int findIntFormat(String date) {
		int informat = 0;
		if (date.length() == 4) {
			informat = 1;
		} else if (date.length() == 7) {
			informat = 2;
		} else if (date.length() == 10) {
			informat = 3;
		} else if (date.length() == 13) {
			informat = 4;
		}
		return informat;
	}
	
	public static long getDateTimeLong(String date) {
		return getDateTimeLong(date, findFormat(date).toPattern());
	}
	
	public static long getDateTimeLong(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static String getDateString(long lDate) {		
		String strFormat = "yyyy-MM-dd HH:mm:ss";
		return new SimpleDateFormat(strFormat).format(new Date(lDate));
	}
}
