package com.yunrang.location.service.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yunrang.location.service.support.LocationService;

public class UtilLocationServiceContextSingaletonFactory {
	private static ApplicationContext locationApplicationContext;
	
	static {
		locationApplicationContext = new ClassPathXmlApplicationContext("spring/snippet_service.xml");
	}
	
	public static ApplicationContext getInstance() {
		return locationApplicationContext;
	}
	
	public static LocationService getLocationServiceBean() {
		return locationApplicationContext.getBean("locationService", LocationService.class);
	}
}
