package com.yunrang.location.integration.support.api.ip;


import org.apache.commons.httpclient.methods.GetMethod;

import com.yunrang.location.common.bean.ProfileCity;
import com.yunrang.location.common.context.ContextHttpClient;
import com.yunrang.location.common.context.ContextPoiQuery;


public class LocationIpApi138 extends LocationIpApi<ProfileCity> {
	private static final String REQUEST_URL = "http://www.ip38.com/index.php?ip=";
	
	private ContextHttpClient contextHttpClient;
    private ContextPoiQuery contextPoiQuery;
	
	//115.25.88.229
	public ProfileCity getResponse(final String ipstr) throws Exception {
		GetMethod method = contextHttpClient.getGetMethod(REQUEST_URL+ipstr);
		String respString = contextHttpClient.invokeHttpRequest(method, "GBK");
		return parseRespString(respString, ipstr);
	}
	
	/**
	 * IP地址<font color=#A7A7A7>(115.25.88.229)</font>查询结果：北京市 教育网            </font></h3>
	 */
	private ProfileCity parseRespString(String respString, String ipstr) {
		String indicatorHead = "IP地址<font color=#A7A7A7>("+ipstr+")</font>查询结果：";
		String indicatorTail = "</font></h3>";
		respString = respString.substring(respString.indexOf(indicatorHead)).replace(indicatorHead, "");
		String addressStr = respString.substring(0, respString.indexOf(indicatorTail));
		return contextPoiQuery.resolveToCityProfile(addressStr);
	}

	public void setContextHttpClient(ContextHttpClient contextHttpClient) {
		this.contextHttpClient = contextHttpClient;
	}
	public void setContextPoiQuery(ContextPoiQuery contextPoiQuery) {
		this.contextPoiQuery = contextPoiQuery;
	}
}
