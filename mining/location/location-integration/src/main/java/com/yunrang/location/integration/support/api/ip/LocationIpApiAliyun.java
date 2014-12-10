package com.yunrang.location.integration.support.api.ip;

import java.util.Iterator;

import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunrang.location.common.bean.ProfileCity;
import com.yunrang.location.common.context.ContextHttpClient;
import com.yunrang.location.common.context.ContextPoiQuery;

public class LocationIpApiAliyun extends LocationIpApi<ProfileCity> {
	private static final Logger logger = LoggerFactory.getLogger(LocationIpApiAliyun.class);
	
	private static final String REQUEST_URL = "http://basic.ditu.aliyun.com/district?it=i&a=nk&sc=2&ct=1&rl=2&h=1&ip=";

    private final ObjectMapper JSON_MAPPER = new ObjectMapper();
    
    private ContextHttpClient contextHttpClient;
    private ContextPoiQuery contextPoiQuery;
    
    /**
     * request ip: 211.138.125.213
     * respones string:
     * ali:{"list":[{"n":"浙江","k":"杭州","c":11,"C":[{"i":"330100","n":"杭州","c":6,"C":[{"i":"330101","n":"市辖区"},{"i":"330122","n":"桐庐"},{"i":"330127","n":"淳安"},
     *      {"i":"330182","n":"建德"},{"i":"330183","n":"富阳"},{"i":"330185","n":"临安"}]},{"i":"330200","n":"宁波","c":6,"C":[{"i":"330201","n":"市辖区"},
     *      {"i":"330225","n":"象山"},{"i":"330226","n":"宁海"},{"i":"330281","n":"余姚"},{"i":"330282","n":"慈溪"},{"i":"330283","n":"奉化"}]},
     *      {"i":"330300","n":"温州","c":9,"C":[{"i":"330301","n":"市辖区"},{"i":"330322","n":"洞头"},{"i":"330324","n":"永嘉"},{"i":"330326","n":"平阳"},
     *      {"i":"330327","n":"苍南"},{"i":"330328","n":"文成"},{"i":"330329","n":"泰顺"},{"i":"330381","n":"瑞安"},{"i":"330382","n":"乐清"}]},
     *      {"i":"330400","n":"嘉兴","c":6,"C":[{"i":"330401","n":"市辖区"},{"i":"330421","n":"嘉善"},{"i":"330424","n":"海盐"},{"i":"330481","n":"海宁"},
     *      {"i":"330482","n":"平湖"},{"i":"330483","n":"桐乡"}]},{"i":"330500","n":"湖州","c":4,"C":[{"i":"330501","n":"市辖区"},{"i":"330521","n":"德清"},
     *      {"i":"330522","n":"长兴"},{"i":"330523","n":"安吉"}]},{"i":"330600","n":"绍兴","c":6,"C":[{"i":"330601","n":"市辖区"},{"i":"330621","n":"绍兴"},
     *      {"i":"330624","n":"新昌"},{"i":"330681","n":"诸暨"},{"i":"330682","n":"上虞"},{"i":"330683","n":"嵊州"}]},{"i":"330700","n":"金华","c":8,
     *      "C":[{"i":"330701","n":"市辖区"},{"i":"330723","n":"武义"},{"i":"330726","n":"浦江"},{"i":"330727","n":"磐安"},{"i":"330781","n":"兰溪"},
     *      {"i":"330782","n":"义乌"},{"i":"330783","n":"东阳"},{"i":"330784","n":"永康"}]},{"i":"330800","n":"衢州","c":5,"C":[{"i":"330801","n":"市辖区"},
     *      {"i":"330822","n":"常山"},{"i":"330824","n":"开化"},{"i":"330825","n":"龙游"},{"i":"330881","n":"江山"}]},{"i":"330900","n":"舟山","c":3,
     *      "C":[{"i":"330901","n":"市辖区"},{"i":"330921","n":"岱山"},{"i":"330922","n":"嵊泗"}]},{"i":"331000","n":"台州","c":7,"C":[{"i":"331001","n":"市辖区"},
     *      {"i":"331021","n":"玉环"},{"i":"331022","n":"三门"},{"i":"331023","n":"天台"},{"i":"331024","n":"仙居"},{"i":"331081","n":"温岭"},{"i":"331082","n":"临海"}]},
     *      {"i":"331100","n":"丽水","c":9,"C":[{"i":"331101","n":"市辖区"},{"i":"331121","n":"青田"},{"i":"331122","n":"缙云"},{"i":"331123","n":"遂昌"},
     *      {"i":"331124","n":"松阳"},{"i":"331125","n":"云和"},{"i":"331126","n":"庆元"},{"i":"331127","n":"景宁"},{"i":"331181","n":"龙泉"}]}],
     *      "hotpt":{"adminCode":"330106","name":"保俶路","geo":[30.265160,120.148643]}}]}
     */
	public ProfileCity getResponse(final String ipstr) {
        try {
            String provinceName = null;
            String cityName = null;
            String districtName = null;
                                    
            String request = REQUEST_URL + ipstr;
            GetMethod method = contextHttpClient.getGetMethod(request);
            method.addRequestHeader("Host", "basic.ditu.aliyun.com");
            String respString = contextHttpClient.invokeHttpRequest(method, "UTF-8");
            JsonNode currNode  = JSON_MAPPER.readTree(respString).get("list");
            Iterator<JsonNode> currIter = null;
            //parse province name
            currIter = currNode.getElements();
            if (currIter.hasNext()) {
                currNode = currIter.next();
                String locationName = currNode.get("n").getTextValue();
                if (contextPoiQuery.isProvinceName(locationName)) {
                	String provinceCode = contextPoiQuery.getProvinceCode(locationName);
                	provinceName = contextPoiQuery.getProvinceCanonicalName(provinceCode);
                } else 
                if (contextPoiQuery.isCityName(locationName)) {
                	String cityCode = contextPoiQuery.getCityCode(locationName);
                	String provinceCode = cityCode.substring(0,2);
                	provinceName = contextPoiQuery.getProvinceCanonicalName(provinceCode);
                	cityName = contextPoiQuery.getCityCanonicalName(cityCode);
                } else 
            	if (contextPoiQuery.isDistrictName(locationName)) {
                	String districtCode = contextPoiQuery.getDistrictCode(districtName).get(0);
                	String cityCode = districtCode.substring(0,4);
                	String provinceCode = districtCode.substring(0,2);
                	provinceName = contextPoiQuery.getProvinceCanonicalName(provinceCode);
                	cityName = contextPoiQuery.getCityCanonicalName(cityCode);
                	districtName = contextPoiQuery.getDistrictCanonicalName(districtCode);
                }
            }
            logger.debug(provinceName+":"+cityName+":"+districtName);
            return contextPoiQuery.configResolveCityNameCityProfile(provinceName, cityName, districtName);
        } catch (Exception e) {
            logger.debug(e.getMessage(),e);
        }
        return null;
	}

	public void setContextHttpClient(ContextHttpClient contextHttpClient) {
		this.contextHttpClient = contextHttpClient;
	}
	public void setContextPoiQuery(ContextPoiQuery contextPoiQuery) {
		this.contextPoiQuery = contextPoiQuery;
	}
}