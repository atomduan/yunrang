package com.yunrang.location.service.util;

import java.util.ArrayList;
import java.util.List;

public class UtilLocationResult {
    public static List<String> getProvinceAndCityListResult(String provinceName, String cityName, String districtName) {
        List<String> result = new ArrayList<String>();
        if (provinceName != null) {
            result.add(provinceName);
            if (cityName != null) {
                result.add(provinceName+cityName);
                if (districtName != null) {
                    result.add(provinceName+cityName+districtName);
                }
            }
        }
        return result;
    }
}
