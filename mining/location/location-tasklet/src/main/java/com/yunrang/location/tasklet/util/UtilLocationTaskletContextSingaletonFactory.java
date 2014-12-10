package com.yunrang.location.tasklet.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UtilLocationTaskletContextSingaletonFactory {
	private static ApplicationContext locationApplicationContext;

	static {
		locationApplicationContext = new ClassPathXmlApplicationContext("spring/snippet_tasklet.xml");
	}
	public static ApplicationContext getInstance() {
		return locationApplicationContext;
	}
}
