package com.yunrang.location.datamgr.mysql.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RepoIpReferDoCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RepoIpReferDoCriteria() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIpReferIdIsNull() {
            addCriterion("ip_refer_id is null");
            return (Criteria) this;
        }

        public Criteria andIpReferIdIsNotNull() {
            addCriterion("ip_refer_id is not null");
            return (Criteria) this;
        }

        public Criteria andIpReferIdEqualTo(Integer value) {
            addCriterion("ip_refer_id =", value, "ipReferId");
            return (Criteria) this;
        }

        public Criteria andIpReferIdNotEqualTo(Integer value) {
            addCriterion("ip_refer_id <>", value, "ipReferId");
            return (Criteria) this;
        }

        public Criteria andIpReferIdGreaterThan(Integer value) {
            addCriterion("ip_refer_id >", value, "ipReferId");
            return (Criteria) this;
        }

        public Criteria andIpReferIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ip_refer_id >=", value, "ipReferId");
            return (Criteria) this;
        }

        public Criteria andIpReferIdLessThan(Integer value) {
            addCriterion("ip_refer_id <", value, "ipReferId");
            return (Criteria) this;
        }

        public Criteria andIpReferIdLessThanOrEqualTo(Integer value) {
            addCriterion("ip_refer_id <=", value, "ipReferId");
            return (Criteria) this;
        }

        public Criteria andIpReferIdIn(List<Integer> values) {
            addCriterion("ip_refer_id in", values, "ipReferId");
            return (Criteria) this;
        }

        public Criteria andIpReferIdNotIn(List<Integer> values) {
            addCriterion("ip_refer_id not in", values, "ipReferId");
            return (Criteria) this;
        }

        public Criteria andIpReferIdBetween(Integer value1, Integer value2) {
            addCriterion("ip_refer_id between", value1, value2, "ipReferId");
            return (Criteria) this;
        }

        public Criteria andIpReferIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ip_refer_id not between", value1, value2, "ipReferId");
            return (Criteria) this;
        }

        public Criteria andIpStrIsNull() {
            addCriterion("ip_str is null");
            return (Criteria) this;
        }

        public Criteria andIpStrIsNotNull() {
            addCriterion("ip_str is not null");
            return (Criteria) this;
        }

        public Criteria andIpStrEqualTo(String value) {
            addCriterion("ip_str =", value, "ipStr");
            return (Criteria) this;
        }

        public Criteria andIpStrNotEqualTo(String value) {
            addCriterion("ip_str <>", value, "ipStr");
            return (Criteria) this;
        }

        public Criteria andIpStrGreaterThan(String value) {
            addCriterion("ip_str >", value, "ipStr");
            return (Criteria) this;
        }

        public Criteria andIpStrGreaterThanOrEqualTo(String value) {
            addCriterion("ip_str >=", value, "ipStr");
            return (Criteria) this;
        }

        public Criteria andIpStrLessThan(String value) {
            addCriterion("ip_str <", value, "ipStr");
            return (Criteria) this;
        }

        public Criteria andIpStrLessThanOrEqualTo(String value) {
            addCriterion("ip_str <=", value, "ipStr");
            return (Criteria) this;
        }

        public Criteria andIpStrLike(String value) {
            addCriterion("ip_str like", value, "ipStr");
            return (Criteria) this;
        }

        public Criteria andIpStrNotLike(String value) {
            addCriterion("ip_str not like", value, "ipStr");
            return (Criteria) this;
        }

        public Criteria andIpStrIn(List<String> values) {
            addCriterion("ip_str in", values, "ipStr");
            return (Criteria) this;
        }

        public Criteria andIpStrNotIn(List<String> values) {
            addCriterion("ip_str not in", values, "ipStr");
            return (Criteria) this;
        }

        public Criteria andIpStrBetween(String value1, String value2) {
            addCriterion("ip_str between", value1, value2, "ipStr");
            return (Criteria) this;
        }

        public Criteria andIpStrNotBetween(String value1, String value2) {
            addCriterion("ip_str not between", value1, value2, "ipStr");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeIsNull() {
            addCriterion("province_code is null");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeIsNotNull() {
            addCriterion("province_code is not null");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeEqualTo(String value) {
            addCriterion("province_code =", value, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeNotEqualTo(String value) {
            addCriterion("province_code <>", value, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeGreaterThan(String value) {
            addCriterion("province_code >", value, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeGreaterThanOrEqualTo(String value) {
            addCriterion("province_code >=", value, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeLessThan(String value) {
            addCriterion("province_code <", value, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeLessThanOrEqualTo(String value) {
            addCriterion("province_code <=", value, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeLike(String value) {
            addCriterion("province_code like", value, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeNotLike(String value) {
            addCriterion("province_code not like", value, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeIn(List<String> values) {
            addCriterion("province_code in", values, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeNotIn(List<String> values) {
            addCriterion("province_code not in", values, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeBetween(String value1, String value2) {
            addCriterion("province_code between", value1, value2, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeNotBetween(String value1, String value2) {
            addCriterion("province_code not between", value1, value2, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceNameIsNull() {
            addCriterion("province_name is null");
            return (Criteria) this;
        }

        public Criteria andProvinceNameIsNotNull() {
            addCriterion("province_name is not null");
            return (Criteria) this;
        }

        public Criteria andProvinceNameEqualTo(String value) {
            addCriterion("province_name =", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameNotEqualTo(String value) {
            addCriterion("province_name <>", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameGreaterThan(String value) {
            addCriterion("province_name >", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameGreaterThanOrEqualTo(String value) {
            addCriterion("province_name >=", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameLessThan(String value) {
            addCriterion("province_name <", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameLessThanOrEqualTo(String value) {
            addCriterion("province_name <=", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameLike(String value) {
            addCriterion("province_name like", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameNotLike(String value) {
            addCriterion("province_name not like", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameIn(List<String> values) {
            addCriterion("province_name in", values, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameNotIn(List<String> values) {
            addCriterion("province_name not in", values, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameBetween(String value1, String value2) {
            addCriterion("province_name between", value1, value2, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameNotBetween(String value1, String value2) {
            addCriterion("province_name not between", value1, value2, "provinceName");
            return (Criteria) this;
        }

        public Criteria andCityCodeIsNull() {
            addCriterion("city_code is null");
            return (Criteria) this;
        }

        public Criteria andCityCodeIsNotNull() {
            addCriterion("city_code is not null");
            return (Criteria) this;
        }

        public Criteria andCityCodeEqualTo(String value) {
            addCriterion("city_code =", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeNotEqualTo(String value) {
            addCriterion("city_code <>", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeGreaterThan(String value) {
            addCriterion("city_code >", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeGreaterThanOrEqualTo(String value) {
            addCriterion("city_code >=", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeLessThan(String value) {
            addCriterion("city_code <", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeLessThanOrEqualTo(String value) {
            addCriterion("city_code <=", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeLike(String value) {
            addCriterion("city_code like", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeNotLike(String value) {
            addCriterion("city_code not like", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeIn(List<String> values) {
            addCriterion("city_code in", values, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeNotIn(List<String> values) {
            addCriterion("city_code not in", values, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeBetween(String value1, String value2) {
            addCriterion("city_code between", value1, value2, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeNotBetween(String value1, String value2) {
            addCriterion("city_code not between", value1, value2, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityNameIsNull() {
            addCriterion("city_name is null");
            return (Criteria) this;
        }

        public Criteria andCityNameIsNotNull() {
            addCriterion("city_name is not null");
            return (Criteria) this;
        }

        public Criteria andCityNameEqualTo(String value) {
            addCriterion("city_name =", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameNotEqualTo(String value) {
            addCriterion("city_name <>", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameGreaterThan(String value) {
            addCriterion("city_name >", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameGreaterThanOrEqualTo(String value) {
            addCriterion("city_name >=", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameLessThan(String value) {
            addCriterion("city_name <", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameLessThanOrEqualTo(String value) {
            addCriterion("city_name <=", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameLike(String value) {
            addCriterion("city_name like", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameNotLike(String value) {
            addCriterion("city_name not like", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameIn(List<String> values) {
            addCriterion("city_name in", values, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameNotIn(List<String> values) {
            addCriterion("city_name not in", values, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameBetween(String value1, String value2) {
            addCriterion("city_name between", value1, value2, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameNotBetween(String value1, String value2) {
            addCriterion("city_name not between", value1, value2, "cityName");
            return (Criteria) this;
        }

        public Criteria andDistrictCodeIsNull() {
            addCriterion("district_code is null");
            return (Criteria) this;
        }

        public Criteria andDistrictCodeIsNotNull() {
            addCriterion("district_code is not null");
            return (Criteria) this;
        }

        public Criteria andDistrictCodeEqualTo(String value) {
            addCriterion("district_code =", value, "districtCode");
            return (Criteria) this;
        }

        public Criteria andDistrictCodeNotEqualTo(String value) {
            addCriterion("district_code <>", value, "districtCode");
            return (Criteria) this;
        }

        public Criteria andDistrictCodeGreaterThan(String value) {
            addCriterion("district_code >", value, "districtCode");
            return (Criteria) this;
        }

        public Criteria andDistrictCodeGreaterThanOrEqualTo(String value) {
            addCriterion("district_code >=", value, "districtCode");
            return (Criteria) this;
        }

        public Criteria andDistrictCodeLessThan(String value) {
            addCriterion("district_code <", value, "districtCode");
            return (Criteria) this;
        }

        public Criteria andDistrictCodeLessThanOrEqualTo(String value) {
            addCriterion("district_code <=", value, "districtCode");
            return (Criteria) this;
        }

        public Criteria andDistrictCodeLike(String value) {
            addCriterion("district_code like", value, "districtCode");
            return (Criteria) this;
        }

        public Criteria andDistrictCodeNotLike(String value) {
            addCriterion("district_code not like", value, "districtCode");
            return (Criteria) this;
        }

        public Criteria andDistrictCodeIn(List<String> values) {
            addCriterion("district_code in", values, "districtCode");
            return (Criteria) this;
        }

        public Criteria andDistrictCodeNotIn(List<String> values) {
            addCriterion("district_code not in", values, "districtCode");
            return (Criteria) this;
        }

        public Criteria andDistrictCodeBetween(String value1, String value2) {
            addCriterion("district_code between", value1, value2, "districtCode");
            return (Criteria) this;
        }

        public Criteria andDistrictCodeNotBetween(String value1, String value2) {
            addCriterion("district_code not between", value1, value2, "districtCode");
            return (Criteria) this;
        }

        public Criteria andDistrictNameIsNull() {
            addCriterion("district_name is null");
            return (Criteria) this;
        }

        public Criteria andDistrictNameIsNotNull() {
            addCriterion("district_name is not null");
            return (Criteria) this;
        }

        public Criteria andDistrictNameEqualTo(String value) {
            addCriterion("district_name =", value, "districtName");
            return (Criteria) this;
        }

        public Criteria andDistrictNameNotEqualTo(String value) {
            addCriterion("district_name <>", value, "districtName");
            return (Criteria) this;
        }

        public Criteria andDistrictNameGreaterThan(String value) {
            addCriterion("district_name >", value, "districtName");
            return (Criteria) this;
        }

        public Criteria andDistrictNameGreaterThanOrEqualTo(String value) {
            addCriterion("district_name >=", value, "districtName");
            return (Criteria) this;
        }

        public Criteria andDistrictNameLessThan(String value) {
            addCriterion("district_name <", value, "districtName");
            return (Criteria) this;
        }

        public Criteria andDistrictNameLessThanOrEqualTo(String value) {
            addCriterion("district_name <=", value, "districtName");
            return (Criteria) this;
        }

        public Criteria andDistrictNameLike(String value) {
            addCriterion("district_name like", value, "districtName");
            return (Criteria) this;
        }

        public Criteria andDistrictNameNotLike(String value) {
            addCriterion("district_name not like", value, "districtName");
            return (Criteria) this;
        }

        public Criteria andDistrictNameIn(List<String> values) {
            addCriterion("district_name in", values, "districtName");
            return (Criteria) this;
        }

        public Criteria andDistrictNameNotIn(List<String> values) {
            addCriterion("district_name not in", values, "districtName");
            return (Criteria) this;
        }

        public Criteria andDistrictNameBetween(String value1, String value2) {
            addCriterion("district_name between", value1, value2, "districtName");
            return (Criteria) this;
        }

        public Criteria andDistrictNameNotBetween(String value1, String value2) {
            addCriterion("district_name not between", value1, value2, "districtName");
            return (Criteria) this;
        }

        public Criteria andIpRangeIdIsNull() {
            addCriterion("ip_range_id is null");
            return (Criteria) this;
        }

        public Criteria andIpRangeIdIsNotNull() {
            addCriterion("ip_range_id is not null");
            return (Criteria) this;
        }

        public Criteria andIpRangeIdEqualTo(Integer value) {
            addCriterion("ip_range_id =", value, "ipRangeId");
            return (Criteria) this;
        }

        public Criteria andIpRangeIdNotEqualTo(Integer value) {
            addCriterion("ip_range_id <>", value, "ipRangeId");
            return (Criteria) this;
        }

        public Criteria andIpRangeIdGreaterThan(Integer value) {
            addCriterion("ip_range_id >", value, "ipRangeId");
            return (Criteria) this;
        }

        public Criteria andIpRangeIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ip_range_id >=", value, "ipRangeId");
            return (Criteria) this;
        }

        public Criteria andIpRangeIdLessThan(Integer value) {
            addCriterion("ip_range_id <", value, "ipRangeId");
            return (Criteria) this;
        }

        public Criteria andIpRangeIdLessThanOrEqualTo(Integer value) {
            addCriterion("ip_range_id <=", value, "ipRangeId");
            return (Criteria) this;
        }

        public Criteria andIpRangeIdIn(List<Integer> values) {
            addCriterion("ip_range_id in", values, "ipRangeId");
            return (Criteria) this;
        }

        public Criteria andIpRangeIdNotIn(List<Integer> values) {
            addCriterion("ip_range_id not in", values, "ipRangeId");
            return (Criteria) this;
        }

        public Criteria andIpRangeIdBetween(Integer value1, Integer value2) {
            addCriterion("ip_range_id between", value1, value2, "ipRangeId");
            return (Criteria) this;
        }

        public Criteria andIpRangeIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ip_range_id not between", value1, value2, "ipRangeId");
            return (Criteria) this;
        }

        public Criteria andTupleStatusIsNull() {
            addCriterion("tuple_status is null");
            return (Criteria) this;
        }

        public Criteria andTupleStatusIsNotNull() {
            addCriterion("tuple_status is not null");
            return (Criteria) this;
        }

        public Criteria andTupleStatusEqualTo(Integer value) {
            addCriterion("tuple_status =", value, "tupleStatus");
            return (Criteria) this;
        }

        public Criteria andTupleStatusNotEqualTo(Integer value) {
            addCriterion("tuple_status <>", value, "tupleStatus");
            return (Criteria) this;
        }

        public Criteria andTupleStatusGreaterThan(Integer value) {
            addCriterion("tuple_status >", value, "tupleStatus");
            return (Criteria) this;
        }

        public Criteria andTupleStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("tuple_status >=", value, "tupleStatus");
            return (Criteria) this;
        }

        public Criteria andTupleStatusLessThan(Integer value) {
            addCriterion("tuple_status <", value, "tupleStatus");
            return (Criteria) this;
        }

        public Criteria andTupleStatusLessThanOrEqualTo(Integer value) {
            addCriterion("tuple_status <=", value, "tupleStatus");
            return (Criteria) this;
        }

        public Criteria andTupleStatusIn(List<Integer> values) {
            addCriterion("tuple_status in", values, "tupleStatus");
            return (Criteria) this;
        }

        public Criteria andTupleStatusNotIn(List<Integer> values) {
            addCriterion("tuple_status not in", values, "tupleStatus");
            return (Criteria) this;
        }

        public Criteria andTupleStatusBetween(Integer value1, Integer value2) {
            addCriterion("tuple_status between", value1, value2, "tupleStatus");
            return (Criteria) this;
        }

        public Criteria andTupleStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("tuple_status not between", value1, value2, "tupleStatus");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }

        public Criteria andIpStrLikeInsensitive(String value) {
            addCriterion("upper(ip_str) like", value.toUpperCase(), "ipStr");
            return this;
        }

        public Criteria andProvinceCodeLikeInsensitive(String value) {
            addCriterion("upper(province_code) like", value.toUpperCase(), "provinceCode");
            return this;
        }

        public Criteria andProvinceNameLikeInsensitive(String value) {
            addCriterion("upper(province_name) like", value.toUpperCase(), "provinceName");
            return this;
        }

        public Criteria andCityCodeLikeInsensitive(String value) {
            addCriterion("upper(city_code) like", value.toUpperCase(), "cityCode");
            return this;
        }

        public Criteria andCityNameLikeInsensitive(String value) {
            addCriterion("upper(city_name) like", value.toUpperCase(), "cityName");
            return this;
        }

        public Criteria andDistrictCodeLikeInsensitive(String value) {
            addCriterion("upper(district_code) like", value.toUpperCase(), "districtCode");
            return this;
        }

        public Criteria andDistrictNameLikeInsensitive(String value) {
            addCriterion("upper(district_name) like", value.toUpperCase(), "districtName");
            return this;
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}