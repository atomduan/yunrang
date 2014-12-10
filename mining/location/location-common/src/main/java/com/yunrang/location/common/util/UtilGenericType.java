package com.yunrang.location.common.util;

public class UtilGenericType {
	public static Class<?> getActrualGenericClass(Class<?> clazzWithGenericType) throws Exception {
		String gss = clazzWithGenericType.getGenericSuperclass().toString();
		String genericTypeName = gss.substring(gss.lastIndexOf("<")+1, gss.lastIndexOf(">"));
		return Class.forName(genericTypeName);
	}
}
