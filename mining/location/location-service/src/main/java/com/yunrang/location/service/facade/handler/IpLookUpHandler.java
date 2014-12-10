package com.yunrang.location.service.facade.handler;


import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.springframework.context.ApplicationContext;

import com.yunrang.location.common.util.UtilPattern;
import com.yunrang.location.service.facade.LocationServiceNetty.RequestHandler;
import com.yunrang.location.service.support.LocationService;

/**
 * uri pattern : /ipLookUp?ip=127.0.0.1
 */
public class IpLookUpHandler implements RequestHandler {
	private final ObjectMapper JSON_MAPPER = new ObjectMapper();
	private LocationService locationService;
	
	public IpLookUpHandler(ApplicationContext locationApplicationContext) throws Exception {
		this.locationService = locationApplicationContext.getBean("locationService", LocationService.class);
	}
	
	@Override
	public String handle(HttpRequest request) {
		String uri = StringUtils.trimToNull(request.getUri());
		try {
			String ipStr = UtilPattern.extractFirst(uri, "ip="+UtilPattern.REGEX_IP).replace("ip=", "");
			List<String> resultList = locationService.getProvinceAndCity(ipStr);
			return JSON_MAPPER.writeValueAsString(resultList);
		} catch (Exception e) {
			return "there is no location recorde found";
		}
	}
}
