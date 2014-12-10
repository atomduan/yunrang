package com.yunrang.location.datamgr.mysql.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RepoIpRangeDoCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RepoIpRangeDoCriteria() {
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

        public Criteria andRetIsNull() {
            addCriterion("ret is null");
            return (Criteria) this;
        }

        public Criteria andRetIsNotNull() {
            addCriterion("ret is not null");
            return (Criteria) this;
        }

        public Criteria andRetEqualTo(String value) {
            addCriterion("ret =", value, "ret");
            return (Criteria) this;
        }

        public Criteria andRetNotEqualTo(String value) {
            addCriterion("ret <>", value, "ret");
            return (Criteria) this;
        }

        public Criteria andRetGreaterThan(String value) {
            addCriterion("ret >", value, "ret");
            return (Criteria) this;
        }

        public Criteria andRetGreaterThanOrEqualTo(String value) {
            addCriterion("ret >=", value, "ret");
            return (Criteria) this;
        }

        public Criteria andRetLessThan(String value) {
            addCriterion("ret <", value, "ret");
            return (Criteria) this;
        }

        public Criteria andRetLessThanOrEqualTo(String value) {
            addCriterion("ret <=", value, "ret");
            return (Criteria) this;
        }

        public Criteria andRetLike(String value) {
            addCriterion("ret like", value, "ret");
            return (Criteria) this;
        }

        public Criteria andRetNotLike(String value) {
            addCriterion("ret not like", value, "ret");
            return (Criteria) this;
        }

        public Criteria andRetIn(List<String> values) {
            addCriterion("ret in", values, "ret");
            return (Criteria) this;
        }

        public Criteria andRetNotIn(List<String> values) {
            addCriterion("ret not in", values, "ret");
            return (Criteria) this;
        }

        public Criteria andRetBetween(String value1, String value2) {
            addCriterion("ret between", value1, value2, "ret");
            return (Criteria) this;
        }

        public Criteria andRetNotBetween(String value1, String value2) {
            addCriterion("ret not between", value1, value2, "ret");
            return (Criteria) this;
        }

        public Criteria andIpStrStartIsNull() {
            addCriterion("ip_str_start is null");
            return (Criteria) this;
        }

        public Criteria andIpStrStartIsNotNull() {
            addCriterion("ip_str_start is not null");
            return (Criteria) this;
        }

        public Criteria andIpStrStartEqualTo(String value) {
            addCriterion("ip_str_start =", value, "ipStrStart");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNotEqualTo(String value) {
            addCriterion("ip_str_start <>", value, "ipStrStart");
            return (Criteria) this;
        }

        public Criteria andIpStrStartGreaterThan(String value) {
            addCriterion("ip_str_start >", value, "ipStrStart");
            return (Criteria) this;
        }

        public Criteria andIpStrStartGreaterThanOrEqualTo(String value) {
            addCriterion("ip_str_start >=", value, "ipStrStart");
            return (Criteria) this;
        }

        public Criteria andIpStrStartLessThan(String value) {
            addCriterion("ip_str_start <", value, "ipStrStart");
            return (Criteria) this;
        }

        public Criteria andIpStrStartLessThanOrEqualTo(String value) {
            addCriterion("ip_str_start <=", value, "ipStrStart");
            return (Criteria) this;
        }

        public Criteria andIpStrStartLike(String value) {
            addCriterion("ip_str_start like", value, "ipStrStart");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNotLike(String value) {
            addCriterion("ip_str_start not like", value, "ipStrStart");
            return (Criteria) this;
        }

        public Criteria andIpStrStartIn(List<String> values) {
            addCriterion("ip_str_start in", values, "ipStrStart");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNotIn(List<String> values) {
            addCriterion("ip_str_start not in", values, "ipStrStart");
            return (Criteria) this;
        }

        public Criteria andIpStrStartBetween(String value1, String value2) {
            addCriterion("ip_str_start between", value1, value2, "ipStrStart");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNotBetween(String value1, String value2) {
            addCriterion("ip_str_start not between", value1, value2, "ipStrStart");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNumIsNull() {
            addCriterion("ip_str_start_num is null");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNumIsNotNull() {
            addCriterion("ip_str_start_num is not null");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNumEqualTo(Long value) {
            addCriterion("ip_str_start_num =", value, "ipStrStartNum");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNumNotEqualTo(Long value) {
            addCriterion("ip_str_start_num <>", value, "ipStrStartNum");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNumGreaterThan(Long value) {
            addCriterion("ip_str_start_num >", value, "ipStrStartNum");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNumGreaterThanOrEqualTo(Long value) {
            addCriterion("ip_str_start_num >=", value, "ipStrStartNum");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNumLessThan(Long value) {
            addCriterion("ip_str_start_num <", value, "ipStrStartNum");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNumLessThanOrEqualTo(Long value) {
            addCriterion("ip_str_start_num <=", value, "ipStrStartNum");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNumIn(List<Long> values) {
            addCriterion("ip_str_start_num in", values, "ipStrStartNum");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNumNotIn(List<Long> values) {
            addCriterion("ip_str_start_num not in", values, "ipStrStartNum");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNumBetween(Long value1, Long value2) {
            addCriterion("ip_str_start_num between", value1, value2, "ipStrStartNum");
            return (Criteria) this;
        }

        public Criteria andIpStrStartNumNotBetween(Long value1, Long value2) {
            addCriterion("ip_str_start_num not between", value1, value2, "ipStrStartNum");
            return (Criteria) this;
        }

        public Criteria andIpStrEndIsNull() {
            addCriterion("ip_str_end is null");
            return (Criteria) this;
        }

        public Criteria andIpStrEndIsNotNull() {
            addCriterion("ip_str_end is not null");
            return (Criteria) this;
        }

        public Criteria andIpStrEndEqualTo(String value) {
            addCriterion("ip_str_end =", value, "ipStrEnd");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNotEqualTo(String value) {
            addCriterion("ip_str_end <>", value, "ipStrEnd");
            return (Criteria) this;
        }

        public Criteria andIpStrEndGreaterThan(String value) {
            addCriterion("ip_str_end >", value, "ipStrEnd");
            return (Criteria) this;
        }

        public Criteria andIpStrEndGreaterThanOrEqualTo(String value) {
            addCriterion("ip_str_end >=", value, "ipStrEnd");
            return (Criteria) this;
        }

        public Criteria andIpStrEndLessThan(String value) {
            addCriterion("ip_str_end <", value, "ipStrEnd");
            return (Criteria) this;
        }

        public Criteria andIpStrEndLessThanOrEqualTo(String value) {
            addCriterion("ip_str_end <=", value, "ipStrEnd");
            return (Criteria) this;
        }

        public Criteria andIpStrEndLike(String value) {
            addCriterion("ip_str_end like", value, "ipStrEnd");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNotLike(String value) {
            addCriterion("ip_str_end not like", value, "ipStrEnd");
            return (Criteria) this;
        }

        public Criteria andIpStrEndIn(List<String> values) {
            addCriterion("ip_str_end in", values, "ipStrEnd");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNotIn(List<String> values) {
            addCriterion("ip_str_end not in", values, "ipStrEnd");
            return (Criteria) this;
        }

        public Criteria andIpStrEndBetween(String value1, String value2) {
            addCriterion("ip_str_end between", value1, value2, "ipStrEnd");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNotBetween(String value1, String value2) {
            addCriterion("ip_str_end not between", value1, value2, "ipStrEnd");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNumIsNull() {
            addCriterion("ip_str_end_num is null");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNumIsNotNull() {
            addCriterion("ip_str_end_num is not null");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNumEqualTo(Long value) {
            addCriterion("ip_str_end_num =", value, "ipStrEndNum");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNumNotEqualTo(Long value) {
            addCriterion("ip_str_end_num <>", value, "ipStrEndNum");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNumGreaterThan(Long value) {
            addCriterion("ip_str_end_num >", value, "ipStrEndNum");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNumGreaterThanOrEqualTo(Long value) {
            addCriterion("ip_str_end_num >=", value, "ipStrEndNum");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNumLessThan(Long value) {
            addCriterion("ip_str_end_num <", value, "ipStrEndNum");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNumLessThanOrEqualTo(Long value) {
            addCriterion("ip_str_end_num <=", value, "ipStrEndNum");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNumIn(List<Long> values) {
            addCriterion("ip_str_end_num in", values, "ipStrEndNum");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNumNotIn(List<Long> values) {
            addCriterion("ip_str_end_num not in", values, "ipStrEndNum");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNumBetween(Long value1, Long value2) {
            addCriterion("ip_str_end_num between", value1, value2, "ipStrEndNum");
            return (Criteria) this;
        }

        public Criteria andIpStrEndNumNotBetween(Long value1, Long value2) {
            addCriterion("ip_str_end_num not between", value1, value2, "ipStrEndNum");
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

        public Criteria andIspIsNull() {
            addCriterion("isp is null");
            return (Criteria) this;
        }

        public Criteria andIspIsNotNull() {
            addCriterion("isp is not null");
            return (Criteria) this;
        }

        public Criteria andIspEqualTo(String value) {
            addCriterion("isp =", value, "isp");
            return (Criteria) this;
        }

        public Criteria andIspNotEqualTo(String value) {
            addCriterion("isp <>", value, "isp");
            return (Criteria) this;
        }

        public Criteria andIspGreaterThan(String value) {
            addCriterion("isp >", value, "isp");
            return (Criteria) this;
        }

        public Criteria andIspGreaterThanOrEqualTo(String value) {
            addCriterion("isp >=", value, "isp");
            return (Criteria) this;
        }

        public Criteria andIspLessThan(String value) {
            addCriterion("isp <", value, "isp");
            return (Criteria) this;
        }

        public Criteria andIspLessThanOrEqualTo(String value) {
            addCriterion("isp <=", value, "isp");
            return (Criteria) this;
        }

        public Criteria andIspLike(String value) {
            addCriterion("isp like", value, "isp");
            return (Criteria) this;
        }

        public Criteria andIspNotLike(String value) {
            addCriterion("isp not like", value, "isp");
            return (Criteria) this;
        }

        public Criteria andIspIn(List<String> values) {
            addCriterion("isp in", values, "isp");
            return (Criteria) this;
        }

        public Criteria andIspNotIn(List<String> values) {
            addCriterion("isp not in", values, "isp");
            return (Criteria) this;
        }

        public Criteria andIspBetween(String value1, String value2) {
            addCriterion("isp between", value1, value2, "isp");
            return (Criteria) this;
        }

        public Criteria andIspNotBetween(String value1, String value2) {
            addCriterion("isp not between", value1, value2, "isp");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(String value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("type like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("type not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andReferFullCodeIsNull() {
            addCriterion("refer_full_code is null");
            return (Criteria) this;
        }

        public Criteria andReferFullCodeIsNotNull() {
            addCriterion("refer_full_code is not null");
            return (Criteria) this;
        }

        public Criteria andReferFullCodeEqualTo(String value) {
            addCriterion("refer_full_code =", value, "referFullCode");
            return (Criteria) this;
        }

        public Criteria andReferFullCodeNotEqualTo(String value) {
            addCriterion("refer_full_code <>", value, "referFullCode");
            return (Criteria) this;
        }

        public Criteria andReferFullCodeGreaterThan(String value) {
            addCriterion("refer_full_code >", value, "referFullCode");
            return (Criteria) this;
        }

        public Criteria andReferFullCodeGreaterThanOrEqualTo(String value) {
            addCriterion("refer_full_code >=", value, "referFullCode");
            return (Criteria) this;
        }

        public Criteria andReferFullCodeLessThan(String value) {
            addCriterion("refer_full_code <", value, "referFullCode");
            return (Criteria) this;
        }

        public Criteria andReferFullCodeLessThanOrEqualTo(String value) {
            addCriterion("refer_full_code <=", value, "referFullCode");
            return (Criteria) this;
        }

        public Criteria andReferFullCodeLike(String value) {
            addCriterion("refer_full_code like", value, "referFullCode");
            return (Criteria) this;
        }

        public Criteria andReferFullCodeNotLike(String value) {
            addCriterion("refer_full_code not like", value, "referFullCode");
            return (Criteria) this;
        }

        public Criteria andReferFullCodeIn(List<String> values) {
            addCriterion("refer_full_code in", values, "referFullCode");
            return (Criteria) this;
        }

        public Criteria andReferFullCodeNotIn(List<String> values) {
            addCriterion("refer_full_code not in", values, "referFullCode");
            return (Criteria) this;
        }

        public Criteria andReferFullCodeBetween(String value1, String value2) {
            addCriterion("refer_full_code between", value1, value2, "referFullCode");
            return (Criteria) this;
        }

        public Criteria andReferFullCodeNotBetween(String value1, String value2) {
            addCriterion("refer_full_code not between", value1, value2, "referFullCode");
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

        public Criteria andRetLikeInsensitive(String value) {
            addCriterion("upper(ret) like", value.toUpperCase(), "ret");
            return this;
        }

        public Criteria andIpStrStartLikeInsensitive(String value) {
            addCriterion("upper(ip_str_start) like", value.toUpperCase(), "ipStrStart");
            return this;
        }

        public Criteria andIpStrEndLikeInsensitive(String value) {
            addCriterion("upper(ip_str_end) like", value.toUpperCase(), "ipStrEnd");
            return this;
        }

        public Criteria andProvinceNameLikeInsensitive(String value) {
            addCriterion("upper(province_name) like", value.toUpperCase(), "provinceName");
            return this;
        }

        public Criteria andProvinceCodeLikeInsensitive(String value) {
            addCriterion("upper(province_code) like", value.toUpperCase(), "provinceCode");
            return this;
        }

        public Criteria andCityNameLikeInsensitive(String value) {
            addCriterion("upper(city_name) like", value.toUpperCase(), "cityName");
            return this;
        }

        public Criteria andCityCodeLikeInsensitive(String value) {
            addCriterion("upper(city_code) like", value.toUpperCase(), "cityCode");
            return this;
        }

        public Criteria andDistrictNameLikeInsensitive(String value) {
            addCriterion("upper(district_name) like", value.toUpperCase(), "districtName");
            return this;
        }

        public Criteria andDistrictCodeLikeInsensitive(String value) {
            addCriterion("upper(district_code) like", value.toUpperCase(), "districtCode");
            return this;
        }

        public Criteria andIspLikeInsensitive(String value) {
            addCriterion("upper(isp) like", value.toUpperCase(), "isp");
            return this;
        }

        public Criteria andTypeLikeInsensitive(String value) {
            addCriterion("upper(type) like", value.toUpperCase(), "type");
            return this;
        }

        public Criteria andDescriptionLikeInsensitive(String value) {
            addCriterion("upper(description) like", value.toUpperCase(), "description");
            return this;
        }

        public Criteria andReferFullCodeLikeInsensitive(String value) {
            addCriterion("upper(refer_full_code) like", value.toUpperCase(), "referFullCode");
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