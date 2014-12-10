package com.yunrang.location.integration.support.api.ip;

import org.apache.commons.httpclient.methods.PostMethod;

import com.yunrang.location.common.bean.ProfileCity;
import com.yunrang.location.common.context.ContextHttpClient;
import com.yunrang.location.common.context.ContextPoiQuery;

public class LocationIpApiTencent extends LocationIpApi<ProfileCity> {
	private static final String REQUEST_URL = "http://ip.qq.com/cgi-bin/searchip";
	
	private String headerIndicator = "<p>该IP所在地为：<span>";
	private String tailIndicator="</span></p>";
	
	private ContextHttpClient contextHttpClient;
    private ContextPoiQuery contextPoiQuery;
	
	public ProfileCity getResponse(final String ipstr) throws Exception {
		PostMethod method = contextHttpClient.getPostMethod(REQUEST_URL);
		method.addParameter("searchip1", ipstr);
		String respString = contextHttpClient.invokeHttpRequest(method, "GBK");
		return parseRespString(respString);
	}
	
	private ProfileCity parseRespString(String respString) {
		String rawAddressStr = respString.substring(respString.indexOf(headerIndicator));
		String addressStr = rawAddressStr.substring(0, rawAddressStr.indexOf(tailIndicator)).replace(headerIndicator, "").replace("&nbsp", "").replace(";", "");
		return contextPoiQuery.resolveToCityProfile(addressStr);
	}

	public void setHeaderIndicator(String headerIndicator) {
		this.headerIndicator = headerIndicator;
	}
	public void setTailIndicator(String tailIndicator) {
		this.tailIndicator = tailIndicator;
	}
	public void setContextHttpClient(ContextHttpClient contextHttpClient) {
		this.contextHttpClient = contextHttpClient;
	}
	public void setContextPoiQuery(ContextPoiQuery contextPoiQuery) {
		this.contextPoiQuery = contextPoiQuery;
	}
}
