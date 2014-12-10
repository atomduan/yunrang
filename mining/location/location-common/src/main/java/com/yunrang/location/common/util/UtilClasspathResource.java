package com.yunrang.location.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class UtilClasspathResource {
	public static Reader getReader(String classPath) {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(classPath);
		return new BufferedReader(new InputStreamReader(is));
	}
}
