package com.yunrang.location.integration.support.api.cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.yunrang.location.common.bean.ProfileCity;
import com.yunrang.location.common.context.ContextHttpClient;
import com.yunrang.location.common.context.ContextPoiQuery;

/**HttpClientContext httpClientContext;
 *  {   "version": "1.1.0", 
 *      "radio_type": "gsm", 
 *      "request_address": true, 
 *      "decode_pos":true,
 *      "location": 
 *          {   "latitude": 39.08943, 
 *              "longitude": 116.36843, 
 *              "accuracy":678.0 
 *          }, 
 *      "cell_towers": 
 *          [ 
 *              {   "cell_id": 4466, 
 *                  "location_area_code":26630, 
 *                  "mobile_country_code": 460, 
 *                  "mobile_network_code": 0,
 *                  "signal_strength": -60, 
 *              }, 
 *              {   "cell_id": 4466, 
 *                  "location_area_code":25054, 
 *                  "mobile_country_code": 460, 
 *                  "mobile_network_code": 0,
 *                  "signal_strength": -70, 
 *              } 
 *          ], 
 *      "wifi_towers": 
 *          [ 
 *              {   "mac_address":"00:0B:86:27:99:B0", 
 *                  “mac_name”:"Sina-web", 
 *                  "signal_strength": 78, 
 *              }, 
 *              {   "mac_address": "00:0B:86:28:7B:F0", 
 *                  “mac_name”:"CMCC", 
 *                  "signal_strength":90, 
 *              } 
 *          ] 
 *  }
 */
public class LocationAPIWeiboCellStation {
	private static final String SOURCE = "2667205658";
    private final String URL = "https://api.weibo.com/2/location/mobile/get_location.json?source="+SOURCE;
    private final ObjectMapper JSON_MAPPER = new ObjectMapper();
    
    private ContextHttpClient contextHttpClient;
    private ContextPoiQuery contextPoiQuery;

	public ProfileCity getResponse(String OPT, String LAC, String CID) throws Exception {
		Request request = new Request();
    	List<Map<String, String>> cellTowerList = new ArrayList<Map<String, String>>();
    	Map<String, String> cellTower = new HashMap<String, String>();
    	cellTower.put("cell_id", CID);
    	cellTower.put("location_area_code", LAC);
    	cellTower.put("mobile_country_code", (String) OPT.substring(0, 3));
    	cellTower.put("mobile_network_code", OPT.substring(3));
    	cellTowerList.add(cellTower);
    	request.setCell_towers(cellTowerList);
    	return getResponse(request);
    }
	
    public ProfileCity getResponse(final LocationAPIWeiboCellStation.Request request) throws Exception {
		PostMethod method = contextHttpClient.getPostMethod(URL);
        method.addRequestHeader("Content-Type", "application/json");
        method.setRequestEntity(new StringRequestEntity(JSON_MAPPER.writeValueAsString(request), "application/json", "UTF-8"));
        return parseLocationsonNode(contextHttpClient.invokeHttpRequest(method, "GBK"));
	}
    
    /**
     * { "location": 
     *      {   "longitude": 116.30951, 
     *          "latitude": 39.98384, 
     *          "accuracy":49, 
     *          "altitude": 0, 
     *          "altitude_accuracy": 0, 
     *          "address": 
     *              {   "province ":"北京市", 
     *                  "city": "北京市", 
     *                  "citycode": "0010" 
     *                  "district": "海淀区", 
     *                  "addr":"海淀北二街10", 
     *              } 
     *      }, 
     *  }
     */
    private ProfileCity parseLocationsonNode(String respString) throws Exception {
    	ProfileCity  profileCity = new ProfileCity();
        JsonNode location = JSON_MAPPER.readTree(respString).get("location");
        if (location!=null) {
        	profileCity.setLongitude(Double.parseDouble(getNodeTextValue(location, "longitude")));
        	profileCity.setLatitude(Double.parseDouble(getNodeTextValue(location, "latitude")));
        	profileCity.setAccuracy(Double.parseDouble(getNodeTextValue(location, "accuracy")));
            JsonNode address = location.get("address");
            if (address!=null) {
            	String provinceName = getNodeTextValue(address,"province");
            	String provinceCode = contextPoiQuery.getProvinceCode(provinceName);
            	String cityName = getNodeTextValue(address,"city");
            	String cityCode = getNodeTextValue(address,"citycode");
            	String districtCode = provinceName + cityCode;
            	String districtName = getNodeTextValue(address,"district");
            	profileCity.setProvinceCode(provinceCode);
            	profileCity.setProvinceName(provinceName);
            	profileCity.setCityCode(cityCode);
            	profileCity.setCityName(cityName);
            	profileCity.setDistrictCode(districtCode);
            	profileCity.setDistrictName(districtName);
            	profileCity.setOptionalInfo(getNodeTextValue(address,"addr"));
            }
        }
        return profileCity;
    }
    
    private String getNodeTextValue(JsonNode node, String fieldName) {
        JsonNode n = node.get(fieldName);
        if (n != null) {
            return n.getTextValue();
        } else {
            return null;
        }
    }
    
    public static class Request {
        Map<String, Double> location = new HashMap<String, Double>(); 
        List<Map<String, String>> cell_towers = new ArrayList<Map<String, String>>();
        List<Map<String, String>> wifi_towers = new ArrayList<Map<String, String>>();
        public Map<String, Double> getLocation() {return location;}
        public void setLocation(Map<String, Double> location) {this.location = location;}
        public List<Map<String, String>> getCell_towers() {return cell_towers;}
        public void setCell_towers(List<Map<String, String>> cellTowers) {cell_towers = cellTowers;}
        public List<Map<String, String>> getWifi_towers() {return wifi_towers;}
        public void setWifi_towers(List<Map<String, String>> wifiTowers) {wifi_towers = wifiTowers;}
    }

	public void setContextHttpClient(ContextHttpClient contextHttpClient) {
		this.contextHttpClient = contextHttpClient;
	}
	public void setContextPoiQuery(ContextPoiQuery contextPoiQuery) {
		this.contextPoiQuery = contextPoiQuery;
	}
}
