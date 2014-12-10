package com.yunrang.location.datamgr.mysql.model;

import java.util.ArrayList;
import java.util.List;

public class RepoGeoDoCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RepoGeoDoCriteria() {
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

        public Criteria andGeoIdIsNull() {
            addCriterion("geo_id is null");
            return (Criteria) this;
        }

        public Criteria andGeoIdIsNotNull() {
            addCriterion("geo_id is not null");
            return (Criteria) this;
        }

        public Criteria andGeoIdEqualTo(Integer value) {
            addCriterion("geo_id =", value, "geoId");
            return (Criteria) this;
        }

        public Criteria andGeoIdNotEqualTo(Integer value) {
            addCriterion("geo_id <>", value, "geoId");
            return (Criteria) this;
        }

        public Criteria andGeoIdGreaterThan(Integer value) {
            addCriterion("geo_id >", value, "geoId");
            return (Criteria) this;
        }

        public Criteria andGeoIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("geo_id >=", value, "geoId");
            return (Criteria) this;
        }

        public Criteria andGeoIdLessThan(Integer value) {
            addCriterion("geo_id <", value, "geoId");
            return (Criteria) this;
        }

        public Criteria andGeoIdLessThanOrEqualTo(Integer value) {
            addCriterion("geo_id <=", value, "geoId");
            return (Criteria) this;
        }

        public Criteria andGeoIdIn(List<Integer> values) {
            addCriterion("geo_id in", values, "geoId");
            return (Criteria) this;
        }

        public Criteria andGeoIdNotIn(List<Integer> values) {
            addCriterion("geo_id not in", values, "geoId");
            return (Criteria) this;
        }

        public Criteria andGeoIdBetween(Integer value1, Integer value2) {
            addCriterion("geo_id between", value1, value2, "geoId");
            return (Criteria) this;
        }

        public Criteria andGeoIdNotBetween(Integer value1, Integer value2) {
            addCriterion("geo_id not between", value1, value2, "geoId");
            return (Criteria) this;
        }

        public Criteria andOriginGeoTypeIsNull() {
            addCriterion("origin_geo_type is null");
            return (Criteria) this;
        }

        public Criteria andOriginGeoTypeIsNotNull() {
            addCriterion("origin_geo_type is not null");
            return (Criteria) this;
        }

        public Criteria andOriginGeoTypeEqualTo(Integer value) {
            addCriterion("origin_geo_type =", value, "originGeoType");
            return (Criteria) this;
        }

        public Criteria andOriginGeoTypeNotEqualTo(Integer value) {
            addCriterion("origin_geo_type <>", value, "originGeoType");
            return (Criteria) this;
        }

        public Criteria andOriginGeoTypeGreaterThan(Integer value) {
            addCriterion("origin_geo_type >", value, "originGeoType");
            return (Criteria) this;
        }

        public Criteria andOriginGeoTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("origin_geo_type >=", value, "originGeoType");
            return (Criteria) this;
        }

        public Criteria andOriginGeoTypeLessThan(Integer value) {
            addCriterion("origin_geo_type <", value, "originGeoType");
            return (Criteria) this;
        }

        public Criteria andOriginGeoTypeLessThanOrEqualTo(Integer value) {
            addCriterion("origin_geo_type <=", value, "originGeoType");
            return (Criteria) this;
        }

        public Criteria andOriginGeoTypeIn(List<Integer> values) {
            addCriterion("origin_geo_type in", values, "originGeoType");
            return (Criteria) this;
        }

        public Criteria andOriginGeoTypeNotIn(List<Integer> values) {
            addCriterion("origin_geo_type not in", values, "originGeoType");
            return (Criteria) this;
        }

        public Criteria andOriginGeoTypeBetween(Integer value1, Integer value2) {
            addCriterion("origin_geo_type between", value1, value2, "originGeoType");
            return (Criteria) this;
        }

        public Criteria andOriginGeoTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("origin_geo_type not between", value1, value2, "originGeoType");
            return (Criteria) this;
        }

        public Criteria andOriginLongitudeIsNull() {
            addCriterion("origin_longitude is null");
            return (Criteria) this;
        }

        public Criteria andOriginLongitudeIsNotNull() {
            addCriterion("origin_longitude is not null");
            return (Criteria) this;
        }

        public Criteria andOriginLongitudeEqualTo(Double value) {
            addCriterion("origin_longitude =", value, "originLongitude");
            return (Criteria) this;
        }

        public Criteria andOriginLongitudeNotEqualTo(Double value) {
            addCriterion("origin_longitude <>", value, "originLongitude");
            return (Criteria) this;
        }

        public Criteria andOriginLongitudeGreaterThan(Double value) {
            addCriterion("origin_longitude >", value, "originLongitude");
            return (Criteria) this;
        }

        public Criteria andOriginLongitudeGreaterThanOrEqualTo(Double value) {
            addCriterion("origin_longitude >=", value, "originLongitude");
            return (Criteria) this;
        }

        public Criteria andOriginLongitudeLessThan(Double value) {
            addCriterion("origin_longitude <", value, "originLongitude");
            return (Criteria) this;
        }

        public Criteria andOriginLongitudeLessThanOrEqualTo(Double value) {
            addCriterion("origin_longitude <=", value, "originLongitude");
            return (Criteria) this;
        }

        public Criteria andOriginLongitudeIn(List<Double> values) {
            addCriterion("origin_longitude in", values, "originLongitude");
            return (Criteria) this;
        }

        public Criteria andOriginLongitudeNotIn(List<Double> values) {
            addCriterion("origin_longitude not in", values, "originLongitude");
            return (Criteria) this;
        }

        public Criteria andOriginLongitudeBetween(Double value1, Double value2) {
            addCriterion("origin_longitude between", value1, value2, "originLongitude");
            return (Criteria) this;
        }

        public Criteria andOriginLongitudeNotBetween(Double value1, Double value2) {
            addCriterion("origin_longitude not between", value1, value2, "originLongitude");
            return (Criteria) this;
        }

        public Criteria andOriginLatitudeIsNull() {
            addCriterion("origin_latitude is null");
            return (Criteria) this;
        }

        public Criteria andOriginLatitudeIsNotNull() {
            addCriterion("origin_latitude is not null");
            return (Criteria) this;
        }

        public Criteria andOriginLatitudeEqualTo(Double value) {
            addCriterion("origin_latitude =", value, "originLatitude");
            return (Criteria) this;
        }

        public Criteria andOriginLatitudeNotEqualTo(Double value) {
            addCriterion("origin_latitude <>", value, "originLatitude");
            return (Criteria) this;
        }

        public Criteria andOriginLatitudeGreaterThan(Double value) {
            addCriterion("origin_latitude >", value, "originLatitude");
            return (Criteria) this;
        }

        public Criteria andOriginLatitudeGreaterThanOrEqualTo(Double value) {
            addCriterion("origin_latitude >=", value, "originLatitude");
            return (Criteria) this;
        }

        public Criteria andOriginLatitudeLessThan(Double value) {
            addCriterion("origin_latitude <", value, "originLatitude");
            return (Criteria) this;
        }

        public Criteria andOriginLatitudeLessThanOrEqualTo(Double value) {
            addCriterion("origin_latitude <=", value, "originLatitude");
            return (Criteria) this;
        }

        public Criteria andOriginLatitudeIn(List<Double> values) {
            addCriterion("origin_latitude in", values, "originLatitude");
            return (Criteria) this;
        }

        public Criteria andOriginLatitudeNotIn(List<Double> values) {
            addCriterion("origin_latitude not in", values, "originLatitude");
            return (Criteria) this;
        }

        public Criteria andOriginLatitudeBetween(Double value1, Double value2) {
            addCriterion("origin_latitude between", value1, value2, "originLatitude");
            return (Criteria) this;
        }

        public Criteria andOriginLatitudeNotBetween(Double value1, Double value2) {
            addCriterion("origin_latitude not between", value1, value2, "originLatitude");
            return (Criteria) this;
        }

        public Criteria andAdjustGeoTypeIsNull() {
            addCriterion("adjust_geo_type is null");
            return (Criteria) this;
        }

        public Criteria andAdjustGeoTypeIsNotNull() {
            addCriterion("adjust_geo_type is not null");
            return (Criteria) this;
        }

        public Criteria andAdjustGeoTypeEqualTo(Integer value) {
            addCriterion("adjust_geo_type =", value, "adjustGeoType");
            return (Criteria) this;
        }

        public Criteria andAdjustGeoTypeNotEqualTo(Integer value) {
            addCriterion("adjust_geo_type <>", value, "adjustGeoType");
            return (Criteria) this;
        }

        public Criteria andAdjustGeoTypeGreaterThan(Integer value) {
            addCriterion("adjust_geo_type >", value, "adjustGeoType");
            return (Criteria) this;
        }

        public Criteria andAdjustGeoTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("adjust_geo_type >=", value, "adjustGeoType");
            return (Criteria) this;
        }

        public Criteria andAdjustGeoTypeLessThan(Integer value) {
            addCriterion("adjust_geo_type <", value, "adjustGeoType");
            return (Criteria) this;
        }

        public Criteria andAdjustGeoTypeLessThanOrEqualTo(Integer value) {
            addCriterion("adjust_geo_type <=", value, "adjustGeoType");
            return (Criteria) this;
        }

        public Criteria andAdjustGeoTypeIn(List<Integer> values) {
            addCriterion("adjust_geo_type in", values, "adjustGeoType");
            return (Criteria) this;
        }

        public Criteria andAdjustGeoTypeNotIn(List<Integer> values) {
            addCriterion("adjust_geo_type not in", values, "adjustGeoType");
            return (Criteria) this;
        }

        public Criteria andAdjustGeoTypeBetween(Integer value1, Integer value2) {
            addCriterion("adjust_geo_type between", value1, value2, "adjustGeoType");
            return (Criteria) this;
        }

        public Criteria andAdjustGeoTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("adjust_geo_type not between", value1, value2, "adjustGeoType");
            return (Criteria) this;
        }

        public Criteria andAdjustLongitudeIsNull() {
            addCriterion("adjust_longitude is null");
            return (Criteria) this;
        }

        public Criteria andAdjustLongitudeIsNotNull() {
            addCriterion("adjust_longitude is not null");
            return (Criteria) this;
        }

        public Criteria andAdjustLongitudeEqualTo(Double value) {
            addCriterion("adjust_longitude =", value, "adjustLongitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLongitudeNotEqualTo(Double value) {
            addCriterion("adjust_longitude <>", value, "adjustLongitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLongitudeGreaterThan(Double value) {
            addCriterion("adjust_longitude >", value, "adjustLongitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLongitudeGreaterThanOrEqualTo(Double value) {
            addCriterion("adjust_longitude >=", value, "adjustLongitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLongitudeLessThan(Double value) {
            addCriterion("adjust_longitude <", value, "adjustLongitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLongitudeLessThanOrEqualTo(Double value) {
            addCriterion("adjust_longitude <=", value, "adjustLongitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLongitudeIn(List<Double> values) {
            addCriterion("adjust_longitude in", values, "adjustLongitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLongitudeNotIn(List<Double> values) {
            addCriterion("adjust_longitude not in", values, "adjustLongitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLongitudeBetween(Double value1, Double value2) {
            addCriterion("adjust_longitude between", value1, value2, "adjustLongitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLongitudeNotBetween(Double value1, Double value2) {
            addCriterion("adjust_longitude not between", value1, value2, "adjustLongitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLatitudeIsNull() {
            addCriterion("adjust_latitude is null");
            return (Criteria) this;
        }

        public Criteria andAdjustLatitudeIsNotNull() {
            addCriterion("adjust_latitude is not null");
            return (Criteria) this;
        }

        public Criteria andAdjustLatitudeEqualTo(Double value) {
            addCriterion("adjust_latitude =", value, "adjustLatitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLatitudeNotEqualTo(Double value) {
            addCriterion("adjust_latitude <>", value, "adjustLatitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLatitudeGreaterThan(Double value) {
            addCriterion("adjust_latitude >", value, "adjustLatitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLatitudeGreaterThanOrEqualTo(Double value) {
            addCriterion("adjust_latitude >=", value, "adjustLatitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLatitudeLessThan(Double value) {
            addCriterion("adjust_latitude <", value, "adjustLatitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLatitudeLessThanOrEqualTo(Double value) {
            addCriterion("adjust_latitude <=", value, "adjustLatitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLatitudeIn(List<Double> values) {
            addCriterion("adjust_latitude in", values, "adjustLatitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLatitudeNotIn(List<Double> values) {
            addCriterion("adjust_latitude not in", values, "adjustLatitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLatitudeBetween(Double value1, Double value2) {
            addCriterion("adjust_latitude between", value1, value2, "adjustLatitude");
            return (Criteria) this;
        }

        public Criteria andAdjustLatitudeNotBetween(Double value1, Double value2) {
            addCriterion("adjust_latitude not between", value1, value2, "adjustLatitude");
            return (Criteria) this;
        }

        public Criteria andPoiIdIsNull() {
            addCriterion("poi_id is null");
            return (Criteria) this;
        }

        public Criteria andPoiIdIsNotNull() {
            addCriterion("poi_id is not null");
            return (Criteria) this;
        }

        public Criteria andPoiIdEqualTo(Integer value) {
            addCriterion("poi_id =", value, "poiId");
            return (Criteria) this;
        }

        public Criteria andPoiIdNotEqualTo(Integer value) {
            addCriterion("poi_id <>", value, "poiId");
            return (Criteria) this;
        }

        public Criteria andPoiIdGreaterThan(Integer value) {
            addCriterion("poi_id >", value, "poiId");
            return (Criteria) this;
        }

        public Criteria andPoiIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("poi_id >=", value, "poiId");
            return (Criteria) this;
        }

        public Criteria andPoiIdLessThan(Integer value) {
            addCriterion("poi_id <", value, "poiId");
            return (Criteria) this;
        }

        public Criteria andPoiIdLessThanOrEqualTo(Integer value) {
            addCriterion("poi_id <=", value, "poiId");
            return (Criteria) this;
        }

        public Criteria andPoiIdIn(List<Integer> values) {
            addCriterion("poi_id in", values, "poiId");
            return (Criteria) this;
        }

        public Criteria andPoiIdNotIn(List<Integer> values) {
            addCriterion("poi_id not in", values, "poiId");
            return (Criteria) this;
        }

        public Criteria andPoiIdBetween(Integer value1, Integer value2) {
            addCriterion("poi_id between", value1, value2, "poiId");
            return (Criteria) this;
        }

        public Criteria andPoiIdNotBetween(Integer value1, Integer value2) {
            addCriterion("poi_id not between", value1, value2, "poiId");
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
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
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