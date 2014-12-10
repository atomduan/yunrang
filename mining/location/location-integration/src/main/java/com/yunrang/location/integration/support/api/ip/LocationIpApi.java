package com.yunrang.location.integration.support.api.ip;

import java.util.Random;


abstract public class LocationIpApi<T> {
	private final Random waitSecRandom = new Random();
	
	abstract public T getResponse(final String ipstr) throws Exception;
	
	public T getResponseGently(String ipStr, int seedSec) throws Exception {
    	int sec = seedSec>0? seedSec : 5;
    	Thread.sleep((waitSecRandom.nextInt(sec)+sec) * 1000);
    	return this.getResponse(ipStr);
    }
}
