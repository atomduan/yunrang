package com.yunrang.location.common.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.yunrang.location.common.bean.ProfileCity;
import com.yunrang.location.common.bean.ProfileIpRange;
import com.yunrang.location.common.config.PoiConfig;


@SuppressWarnings("serial")
public final class ContextPoiQuery {
    private final Set<String> PROVINCE_NAME_SET = new HashSet<String>();
    private final Set<String> CITY_NAME_SET = new HashSet<String>();
    private final Set<String> DISTRICT_NAME_SET = new HashSet<String>();
    
    private final Map<String, String> CODE_TO_PROVINCE_CANONICAL_NAME_MAP = new HashMap<String, String>();
    private final Map<String, String> CODE_TO_CITY_CANONICAL_NAME_MAP = new HashMap<String, String>();
    private final Map<String, String> CODE_TO_DISTRICT_CANONICAL_NAME_MAP = new HashMap<String, String>();
    
    private final Map<String, String> PROVINCE_NAME_TO_CODE_MAP = new HashMap<String, String>();
    private final Map<String, String> CITY_NAME_TO_CODE_MAP = new HashMap<String, String>();
    private final Map<String, List<String>> DISTRICT_NAME_TO_CODE_MAP = new HashMap<String, List<String>>();
    
    public ContextPoiQuery() {
        this.initProvinceReleventMaps();
        this.initCityReleventMaps();
        this.initDistrictReleventMaps();
    }
    
    private void initDistrictReleventMaps() {
        for (int i = 0; i < PoiConfig.DISTRICT_CODE.length; i++) {
            final String line = PoiConfig.DISTRICT_CODE[i];
            final String[] fields = line.split("#");
            final String districtCode = fields[0].trim();
            final String districtCanonicalName = fields[1].trim();
            CODE_TO_DISTRICT_CANONICAL_NAME_MAP.put(districtCode, districtCanonicalName);
            //Configure districtCanonicalName
            DISTRICT_NAME_SET.add(districtCanonicalName);
            DISTRICT_NAME_TO_CODE_MAP.put(districtCanonicalName, new ArrayList<String>(){{add(districtCode);}});
            //Configure districtAlias
            for (int aliasNameIdx=2; aliasNameIdx<fields.length; aliasNameIdx++) {
                String districtAlias = fields[aliasNameIdx].trim();
                DISTRICT_NAME_SET.add(districtAlias);
                if (false == DISTRICT_NAME_TO_CODE_MAP.containsKey(districtAlias)) {
                    DISTRICT_NAME_TO_CODE_MAP.put(districtAlias, new ArrayList<String>(){{add(districtCode);}});
                } else {
                    DISTRICT_NAME_TO_CODE_MAP.get(districtAlias).add(districtCode);
                }
            }
        }
    }
    
    private void initCityReleventMaps() {
        for (int i = 0; i < PoiConfig.CITY_CODE.length; i++) {
            String line = PoiConfig.CITY_CODE[i];
            String[] fields = line.split("#");
            String cityCode = fields[0].trim();
            String cityCanonicalName = fields[1].trim().split(";")[0];
            String[] cityAliases = fields[1].trim().split(";");
            String[] cityPinYins = fields[2].trim().split(";");
            CODE_TO_CITY_CANONICAL_NAME_MAP.put(cityCode, cityCanonicalName);
            //Configure City_Name_To_Code_Map
            for (String a : cityAliases) {
                CITY_NAME_TO_CODE_MAP.put(a, cityCode);
                CITY_NAME_SET.add(a);
            }
            for (String p : cityPinYins) {
                String lowerCaseName = p.toLowerCase();
                CITY_NAME_TO_CODE_MAP.put(p, cityCode);
                CITY_NAME_TO_CODE_MAP.put(lowerCaseName, cityCode);
                CITY_NAME_SET.add(p);
                CITY_NAME_SET.add(lowerCaseName);
            }
        }
    }
    
    private void initProvinceReleventMaps() {
        for (int i = 0; i < PoiConfig.PROVINCE_CODE.length; i++) {
            String[] fields = PoiConfig.PROVINCE_CODE[i].split("#");
            String provinceCode = fields[0].trim();
            String provinceCanonicalName = fields[1].trim().split(";")[0];
            String[] provinceAliases = fields[1].trim().split(";");
            String[] provincePinYins = fields[2].trim().split(";");
            CODE_TO_PROVINCE_CANONICAL_NAME_MAP.put(provinceCode, provinceCanonicalName);
            //Configure Province_Name_To_Code_Map
            //Configure City_Name_To_Code_Map
            for (String a : provinceAliases) {
                PROVINCE_NAME_TO_CODE_MAP.put(a, provinceCode);
                PROVINCE_NAME_SET.add(a);
            }
            for (String p : provincePinYins) {
                String lowerCaseName = p.toLowerCase();
                PROVINCE_NAME_TO_CODE_MAP.put(p, provinceCode);
                PROVINCE_NAME_TO_CODE_MAP.put(p.toLowerCase(), provinceCode);
                PROVINCE_NAME_SET.add(p);
                PROVINCE_NAME_SET.add(lowerCaseName);
            }
        }
    }
    
    private String preprocessNameString(String name) {
        String result = StringUtils.trimToEmpty(name);
        if (result.matches("[a-zA-Z]+")) {
            result = result.toLowerCase();
        }
        return result;
    }
    
    private static final String[] CHINA_NAMES = new String[] {"中国","中华人民共和国","CN","cn","China","china","Zhong Guo","ZhongGuo"};
    public boolean isChineseProfileIpRange(ProfileIpRange profile) {
        if (profile.getCountryName() != null) {
            for (String n : CHINA_NAMES) {
                if (profile.getCountryName().contains(n)) {
                    return true;
                }
            }
        } else {
            String provinceCode = profile.getProvinceCode();
            if (provinceCode != null && provinceCode.length()>0) {
                return true;
            }
        }
        return false;
    }
    
    public Boolean isDistrictName(String name) {
        return DISTRICT_NAME_SET.contains(preprocessNameString(name));
    }
    
    public Boolean isCityName(String name) {
        return CITY_NAME_SET.contains(preprocessNameString(name));
    }
    
    public Boolean isProvinceName(String name) {
        return PROVINCE_NAME_SET.contains(preprocessNameString(name));
    }
    
    public Boolean isDCCityName(String name) {
        return PoiConfig.DC_CITIES.contains(StringUtils.trimToEmpty(name));
    }
    
    public String getProvinceCanonicalName(String provinceCode) {
        return CODE_TO_PROVINCE_CANONICAL_NAME_MAP.get(StringUtils.trimToEmpty(provinceCode));
    }
    
    public String getCityCanonicalName(String cityCode) {
        return CODE_TO_CITY_CANONICAL_NAME_MAP.get(StringUtils.trimToEmpty(cityCode));
    }
    
    public String getDistrictCanonicalName(String districtCode) {
        return CODE_TO_DISTRICT_CANONICAL_NAME_MAP.get(StringUtils.trimToEmpty(districtCode));
    }
    
    public String getProvinceCode(String provinceName) {
        return PROVINCE_NAME_TO_CODE_MAP.get(StringUtils.trimToEmpty(provinceName));
    }
    
    public String getCityCode(String cityName) {
        return CITY_NAME_TO_CODE_MAP.get(StringUtils.trimToEmpty(cityName));
    }
    
    public List<String> getDistrictCode(String districtName) {
        return DISTRICT_NAME_TO_CODE_MAP.get(StringUtils.trimToEmpty(districtName));
    }
    
    public String getDistrictCode(String districtName, String cityCode) {
    	districtName = StringUtils.trimToEmpty(districtName);
    	cityCode = StringUtils.trimToEmpty(cityCode);
        List<String> provinceCodes = DISTRICT_NAME_TO_CODE_MAP.get(districtName);
        if (provinceCodes != null && cityCode != null) {
            for (String p : provinceCodes) {
                if (p.startsWith(cityCode)) {
                    return p;
                }
            }
        }
        return null;
    }
    
    public ProfileCity resolveToCityProfile (String candidateString) {
        candidateString = StringUtils.trimToEmpty(candidateString);
        Integer policyCount = 3;
        String[] names = new String[policyCount+1];
        if (candidateString.length() > 3) {
            Integer beginCutPoint = 0;
            _NEXT_POLICY:
                for (int policy=0 ; policy<policyCount; policy++) {
                    for (int b=beginCutPoint; b<candidateString.length(); b++) {
                        for (int e=candidateString.length(); e>b; e--) {
                            String cutCandi = candidateString.substring(b,e);
                            if (poiNameCheckCallBack(policy, cutCandi)) {
                                if(consultCityComfirmation(policy, names, cutCandi)) {
                                    beginCutPoint = e;
                                    continue _NEXT_POLICY;
                                }
                            }
                        }
                    }
                }
            // get the tail address info
            names[policyCount] = candidateString.substring(beginCutPoint);
        } else {
            resovleShortAddressName(names, candidateString);
        }
        return configResolveCityNameCityProfile(names);
    }
    
    /**
     * when cutCandi length < 3 use it
     */
    private void resovleShortAddressName(String[] names, String candidateString) {
        String provinceName = null;
        String cityName = null;
        String districtName = null;
        if (DISTRICT_NAME_TO_CODE_MAP.containsKey(candidateString)) {
            List<String> districtCodes = DISTRICT_NAME_TO_CODE_MAP.get(candidateString);
            if (districtCodes.size() == 1) {
                String districtCode = districtCodes.get(0);
                districtName = CODE_TO_DISTRICT_CANONICAL_NAME_MAP.get(districtCode);
                String cityCode = districtCode.substring(0,4);
                cityName = CODE_TO_CITY_CANONICAL_NAME_MAP.get(cityCode);
                String provinceCode = districtCode.substring(0,2);
                provinceName = CODE_TO_PROVINCE_CANONICAL_NAME_MAP.get(provinceCode);
            }
        }
        if (CITY_NAME_TO_CODE_MAP.containsKey(candidateString)) {
            String cityCode = CITY_NAME_TO_CODE_MAP.get(candidateString);
            cityName = CODE_TO_CITY_CANONICAL_NAME_MAP.get(cityCode);
            String provinceCode = cityCode.substring(0,2);
            provinceName = CODE_TO_PROVINCE_CANONICAL_NAME_MAP.get(provinceCode);
        }
        if (PROVINCE_NAME_TO_CODE_MAP.containsKey(candidateString)) {
            String provinceCode = PROVINCE_NAME_TO_CODE_MAP.get(candidateString);
            provinceName = CODE_TO_PROVINCE_CANONICAL_NAME_MAP.get(provinceCode);
        }
        names[0] = provinceName;
        names[1] = cityName;
        names[2] = districtName;
    }
    
    private Boolean consultCityComfirmation(Integer currentPolicy, String[] names, String cutCandi){
        if (currentPolicy == 0) {
            String provinceName = cutCandi;
            String provinceCode = PROVINCE_NAME_TO_CODE_MAP.get(provinceName);
            provinceName = CODE_TO_PROVINCE_CANONICAL_NAME_MAP.get(provinceCode);
            names[0] = provinceName;
            return true;
        } else if (currentPolicy == 1) {
            String provinceName = names[0];
            String cityName = cutCandi;
            String cityCode = CITY_NAME_TO_CODE_MAP.get(cityName);
            if (provinceName != null) {
                String provinceCode = PROVINCE_NAME_TO_CODE_MAP.get(provinceName);
                if (!cityCode.startsWith(provinceCode)) {
                     return false;
                }
            } else {
                String provinceCode = cityCode.substring(0,2);
                provinceName = CODE_TO_PROVINCE_CANONICAL_NAME_MAP.get(provinceCode);
            }
            cityName = CODE_TO_CITY_CANONICAL_NAME_MAP.get(cityCode);
            names[0] = provinceName;
            names[1] = cityName;
            return true;
        } else if (currentPolicy == 2) {
            String provinceName = names[0];
            String cityName = names[1];
            String districtName = cutCandi;
            List<String> districtCodeList = DISTRICT_NAME_TO_CODE_MAP.get(districtName);
            // due to step 2, there is no possible for such case: name[0]==null && name[1]!=null,
            if (cityName != null) {
                String cityCode = CITY_NAME_TO_CODE_MAP.get(cityName);
                for (String code : districtCodeList) {
                    if (code.startsWith(cityCode)) {
                        districtName = CODE_TO_DISTRICT_CANONICAL_NAME_MAP.get(code);
                        break;
                    }
                }
            } else {
                if (provinceName != null) {
                    Boolean isConsistWithProvince = false;
                    String provinceCode = PROVINCE_NAME_TO_CODE_MAP.get(provinceName);
                    for (String code : districtCodeList) {
                        if (code.startsWith(provinceCode)) {
                            districtName = CODE_TO_DISTRICT_CANONICAL_NAME_MAP.get(code);
                            String cityCode = code.substring(0,4);
                            cityName = CODE_TO_CITY_CANONICAL_NAME_MAP.get(cityCode);
                            isConsistWithProvince = true;
                            break;
                        }
                    }
                    if (isConsistWithProvince == false) return false;
                } else {
                    if (districtCodeList.size()==1) {
                        String districtCode = districtCodeList.get(0);
                        districtName = CODE_TO_DISTRICT_CANONICAL_NAME_MAP.get(districtCode);
                        String cityCode = districtCode.substring(0,4);
                        cityName = CODE_TO_CITY_CANONICAL_NAME_MAP.get(cityCode);
                        String provinceCode = districtCode.substring(0,2);
                        provinceName = CODE_TO_PROVINCE_CANONICAL_NAME_MAP.get(provinceCode);
                    }
                }
            }
            names[0] = provinceName;
            names[1] = cityName;
            names[2] = districtName;
            return true;
        }
        return false;
    }
    
    private Boolean poiNameCheckCallBack(Integer policy, String name) {
        switch (policy) {
            case 0 : return isProvinceName(name);
            case 1 : return isCityName(name);
            case 2 : return isDistrictName(name);
            default: return false;
        }
    }
    
    public ProfileCity configResolveCityNameCityProfile(String provinceName, String cityName, String districtName) {
    	return this.configResolveCityNameCityProfile(provinceName, cityName, districtName, null);
    }
    
    public ProfileCity configResolveCityNameCityProfile(String provinceName, String cityName, String districtName, String optionalInfo) {
    	return this.configResolveCityNameCityProfile(new String[]{provinceName, cityName, districtName, optionalInfo});
    }
    
    private ProfileCity configResolveCityNameCityProfile(String[] names) {
        ProfileCity city = new ProfileCity();
        // parse...
        String provinceName = StringUtils.trimToEmpty(names[0]);
        String cityName = StringUtils.trimToEmpty(names[1]);
        //cityName 为空时, 直辖市特别处理
        if (isDCCityName(provinceName) && StringUtils.isBlank(cityName)) {
        	cityName = provinceName;
        }
        String districtName = StringUtils.trimToEmpty(names[2]);
        String optionalInfo = names.length < 4 ? null : StringUtils.trimToEmpty(names[3]);
        String provinceCode = StringUtils.isBlank(provinceName) ? null : PROVINCE_NAME_TO_CODE_MAP.get(provinceName);
        String cityCode = StringUtils.isBlank(cityName) ? null : getCityCode(cityName);
        String districtCode = StringUtils.isBlank(districtName) ?  null : getDistrictCode(districtName, cityCode);
        // fill....
        city.setProvinceName(provinceName);
        city.setCityName(cityName);
        city.setDistrictName(districtName);
        city.setProvinceCode(provinceCode);
        city.setCityCode(cityCode);
        city.setDistrictCode(districtCode);
        city.setOptionalInfo(optionalInfo);
        return city;
    }
}