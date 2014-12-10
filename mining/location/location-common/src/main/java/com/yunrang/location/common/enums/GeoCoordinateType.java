package com.yunrang.location.common.enums;

public enum GeoCoordinateType {
    Undefined_Coordinate    (-1),
    Raw_Coordinate          (0),
    Google_Coordinate       (1),
    Baidu_Coordinate        (2),
    Sina_Coordinate         (3);
    private Integer value;
    GeoCoordinateType(Integer value) {this.value = value;}
    public Integer getValue() {return value;}
    public static GeoCoordinateType getEnum(Integer value) {
        switch (value) {
            case 0 : return Raw_Coordinate;
            case 1 : return Google_Coordinate;
            case 2 : return Baidu_Coordinate;
            case 3 : return Sina_Coordinate;
            default: return null;
        }
    }
}
