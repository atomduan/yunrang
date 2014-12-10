package com.yunrang.location.integration.support.api.ip;

import org.apache.commons.httpclient.methods.GetMethod;

import com.yunrang.location.common.bean.ProfileCity;
import com.yunrang.location.common.context.ContextHttpClient;
import com.yunrang.location.common.context.ContextPoiQuery;

public class LocationIpApiYoudao extends LocationIpApi<ProfileCity> {
	private static final String REQUEST_URL = "http://www.yodao.com/smartresult-xml/search.s?jsFlag=true&keyfrom=163.com&event=fYodaoCallBack&type=ip&q=";
	
	private ContextHttpClient contextHttpClient;
	private ContextPoiQuery contextPoiQuery;
	
	public ProfileCity getResponse(final String ipstr) {
		try {
	        String request = REQUEST_URL + ipstr;
	        GetMethod method = contextHttpClient.getGetMethod(request);
	        method.addRequestHeader("Host", "www.youdao.com");
	        String respString = contextHttpClient.invokeHttpRequest(method, "GBK");
	        return resolveMultiCityName(respString);
	    } catch (Exception e) {
	    }
	    return null;
	}
	
	/**
	 * fYodaoCallBack(1,  {'product':'ip','ip':'117.136.34.1','location':'广西 移动'} , '');
	 */
	private final String headIndicator = "location':'";
	private final String tailIndicator = "'}";
	private ProfileCity resolveMultiCityName(String respString) {
        ProfileCity profile = null;
        String rawAddressStr = respString.substring(respString.indexOf(headIndicator));
        String addressStr = rawAddressStr.substring(0, rawAddressStr.indexOf(tailIndicator)).replace(headIndicator, "").trim();
        if (addressStr != null && addressStr.length()>0) {
            profile = contextPoiQuery.resolveToCityProfile(addressStr);
        }
        return profile;
    }

	public void setContextHttpClient(ContextHttpClient contextHttpClient) {
		this.contextHttpClient = contextHttpClient;
	}
	public void setContextPoiQuery(ContextPoiQuery contextPoiQuery) {
		this.contextPoiQuery = contextPoiQuery;
	}
}
