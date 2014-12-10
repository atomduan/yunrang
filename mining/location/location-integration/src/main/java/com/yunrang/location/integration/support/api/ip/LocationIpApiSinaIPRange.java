package com.yunrang.location.integration.support.api.ip;


import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunrang.location.common.bean.ProfileIpRange;
import com.yunrang.location.common.context.ContextHttpClient;
import com.yunrang.location.common.context.ContextPoiQuery;
import com.yunrang.location.common.util.UtilIpConverter;

public class LocationIpApiSinaIPRange extends LocationIpApi<ProfileIpRange> {
    private static final Logger logger = LoggerFactory.getLogger(LocationIpApiSinaIPRange.class);
    private static final String REQUEST_URL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=";
    private final ObjectMapper JSON_MAPPER = new ObjectMapper();
    
    private ContextHttpClient contextHttpClient;
    private ContextPoiQuery contextPoiQuery;
    
    public ProfileIpRange getResponse(final String ipstr) {
        /**
         * request ip: 211.138.125.213
         * response str:
         * sina:var remote_ip_info = {"ret":1,"start":"211.138.125.0","end":"211.138.125.255",
         *      "country":"\u4e2d\u56fd","province":"\u6d59\u6c5f","city":"\u91d1\u534e","district":"","isp":"\u79fb\u901a","type":"","desc":""};
         */
        try {
            String request = REQUEST_URL + ipstr;
            GetMethod method = contextHttpClient.getGetMethod(request);
            method.addRequestHeader("Host", "int.dpool.sina.com.cn");
            String respString = contextHttpClient.invokeHttpRequest(method, "GBK");
            respString = respString.substring(respString.indexOf("{\""));
            JsonNode currNode  = JSON_MAPPER.readTree(respString);
            return configLocationProfileIpRange(currNode);
        } catch (Exception e) {
            logger.warn("consult exception ipstr["+ipstr+"]", e);
        }
        return null;
    }
    
    private ProfileIpRange configLocationProfileIpRange(JsonNode currNode) {
        // parse
        String ret = currNode.get("ret") != null? currNode.get("ret").asText() : null;
        String ipStrStart = currNode.get("start") != null? currNode.get("start").getTextValue() : null;
        Long ipStrStartNum = UtilIpConverter.toLongValue(ipStrStart);
        String ipStrEnd = currNode.get("end") != null? currNode.get("end").getTextValue() : null;
        Long ipStrEndNum = UtilIpConverter.toLongValue(ipStrEnd);
        String countryName = currNode.get("country") != null ? currNode.get("country").getTextValue() : null;
        String provinceName = currNode.get("province") != null ? currNode.get("province").getTextValue() : null;
        String provinceCode = contextPoiQuery.getProvinceCode(provinceName);
        String cityName = currNode.get("city") != null? currNode.get("city").getTextValue() : null;
        String cityCode = contextPoiQuery.getCityCode(cityName);
        String districtName = currNode.get("district") != null? currNode.get("district").getTextValue() : null;
        String districtCode = contextPoiQuery.getDistrictCode(districtName, cityCode);
        String isp = currNode.get("isp") != null? currNode.get("isp").getTextValue() : null;
        String type = currNode.get("type") != null? currNode.get("type").getTextValue() : null;
        String description = currNode.get("desc") != null? currNode.get("desc").getTextValue() : null;
        // fill
        ProfileIpRange p = new ProfileIpRange();
        p.setRet(ret);
        p.setIpStrStart(ipStrStart);
        p.setIpStrStartNum(ipStrStartNum);
        p.setIpStrEnd(ipStrEnd);
        p.setIpStrEndNum(ipStrEndNum);
        p.setCountryName(countryName);
        p.setProvinceName(provinceName);
        p.setProvinceCode(provinceCode);
        p.setCityName(cityName);
        p.setCityCode(cityCode);
        p.setDistrictName(districtName);
        p.setDistrictCode(districtCode);
        p.setIsp(isp);
        p.setType(type);
        p.setDescription(description);
        return p;
    }

	public void setContextHttpClient(ContextHttpClient contextHttpClient) {
		this.contextHttpClient = contextHttpClient;
	}
	public void setContextPoiQuery(ContextPoiQuery contextPoiQuery) {
		this.contextPoiQuery = contextPoiQuery;
	}
}
