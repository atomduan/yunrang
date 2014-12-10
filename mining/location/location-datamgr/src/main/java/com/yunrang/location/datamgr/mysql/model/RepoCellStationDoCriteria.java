package com.yunrang.location.datamgr.mysql.model;

import java.util.ArrayList;
import java.util.List;

public class RepoCellStationDoCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RepoCellStationDoCriteria() {
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

        public Criteria andCellStationIdIsNull() {
            addCriterion("cell_station_id is null");
            return (Criteria) this;
        }

        public Criteria andCellStationIdIsNotNull() {
            addCriterion("cell_station_id is not null");
            return (Criteria) this;
        }

        public Criteria andCellStationIdEqualTo(Integer value) {
            addCriterion("cell_station_id =", value, "cellStationId");
            return (Criteria) this;
        }

        public Criteria andCellStationIdNotEqualTo(Integer value) {
            addCriterion("cell_station_id <>", value, "cellStationId");
            return (Criteria) this;
        }

        public Criteria andCellStationIdGreaterThan(Integer value) {
            addCriterion("cell_station_id >", value, "cellStationId");
            return (Criteria) this;
        }

        public Criteria andCellStationIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("cell_station_id >=", value, "cellStationId");
            return (Criteria) this;
        }

        public Criteria andCellStationIdLessThan(Integer value) {
            addCriterion("cell_station_id <", value, "cellStationId");
            return (Criteria) this;
        }

        public Criteria andCellStationIdLessThanOrEqualTo(Integer value) {
            addCriterion("cell_station_id <=", value, "cellStationId");
            return (Criteria) this;
        }

        public Criteria andCellStationIdIn(List<Integer> values) {
            addCriterion("cell_station_id in", values, "cellStationId");
            return (Criteria) this;
        }

        public Criteria andCellStationIdNotIn(List<Integer> values) {
            addCriterion("cell_station_id not in", values, "cellStationId");
            return (Criteria) this;
        }

        public Criteria andCellStationIdBetween(Integer value1, Integer value2) {
            addCriterion("cell_station_id between", value1, value2, "cellStationId");
            return (Criteria) this;
        }

        public Criteria andCellStationIdNotBetween(Integer value1, Integer value2) {
            addCriterion("cell_station_id not between", value1, value2, "cellStationId");
            return (Criteria) this;
        }

        public Criteria andMccIsNull() {
            addCriterion("mcc is null");
            return (Criteria) this;
        }

        public Criteria andMccIsNotNull() {
            addCriterion("mcc is not null");
            return (Criteria) this;
        }

        public Criteria andMccEqualTo(Integer value) {
            addCriterion("mcc =", value, "mcc");
            return (Criteria) this;
        }

        public Criteria andMccNotEqualTo(Integer value) {
            addCriterion("mcc <>", value, "mcc");
            return (Criteria) this;
        }

        public Criteria andMccGreaterThan(Integer value) {
            addCriterion("mcc >", value, "mcc");
            return (Criteria) this;
        }

        public Criteria andMccGreaterThanOrEqualTo(Integer value) {
            addCriterion("mcc >=", value, "mcc");
            return (Criteria) this;
        }

        public Criteria andMccLessThan(Integer value) {
            addCriterion("mcc <", value, "mcc");
            return (Criteria) this;
        }

        public Criteria andMccLessThanOrEqualTo(Integer value) {
            addCriterion("mcc <=", value, "mcc");
            return (Criteria) this;
        }

        public Criteria andMccIn(List<Integer> values) {
            addCriterion("mcc in", values, "mcc");
            return (Criteria) this;
        }

        public Criteria andMccNotIn(List<Integer> values) {
            addCriterion("mcc not in", values, "mcc");
            return (Criteria) this;
        }

        public Criteria andMccBetween(Integer value1, Integer value2) {
            addCriterion("mcc between", value1, value2, "mcc");
            return (Criteria) this;
        }

        public Criteria andMccNotBetween(Integer value1, Integer value2) {
            addCriterion("mcc not between", value1, value2, "mcc");
            return (Criteria) this;
        }

        public Criteria andMncIsNull() {
            addCriterion("mnc is null");
            return (Criteria) this;
        }

        public Criteria andMncIsNotNull() {
            addCriterion("mnc is not null");
            return (Criteria) this;
        }

        public Criteria andMncEqualTo(Integer value) {
            addCriterion("mnc =", value, "mnc");
            return (Criteria) this;
        }

        public Criteria andMncNotEqualTo(Integer value) {
            addCriterion("mnc <>", value, "mnc");
            return (Criteria) this;
        }

        public Criteria andMncGreaterThan(Integer value) {
            addCriterion("mnc >", value, "mnc");
            return (Criteria) this;
        }

        public Criteria andMncGreaterThanOrEqualTo(Integer value) {
            addCriterion("mnc >=", value, "mnc");
            return (Criteria) this;
        }

        public Criteria andMncLessThan(Integer value) {
            addCriterion("mnc <", value, "mnc");
            return (Criteria) this;
        }

        public Criteria andMncLessThanOrEqualTo(Integer value) {
            addCriterion("mnc <=", value, "mnc");
            return (Criteria) this;
        }

        public Criteria andMncIn(List<Integer> values) {
            addCriterion("mnc in", values, "mnc");
            return (Criteria) this;
        }

        public Criteria andMncNotIn(List<Integer> values) {
            addCriterion("mnc not in", values, "mnc");
            return (Criteria) this;
        }

        public Criteria andMncBetween(Integer value1, Integer value2) {
            addCriterion("mnc between", value1, value2, "mnc");
            return (Criteria) this;
        }

        public Criteria andMncNotBetween(Integer value1, Integer value2) {
            addCriterion("mnc not between", value1, value2, "mnc");
            return (Criteria) this;
        }

        public Criteria andLacIsNull() {
            addCriterion("lac is null");
            return (Criteria) this;
        }

        public Criteria andLacIsNotNull() {
            addCriterion("lac is not null");
            return (Criteria) this;
        }

        public Criteria andLacEqualTo(Integer value) {
            addCriterion("lac =", value, "lac");
            return (Criteria) this;
        }

        public Criteria andLacNotEqualTo(Integer value) {
            addCriterion("lac <>", value, "lac");
            return (Criteria) this;
        }

        public Criteria andLacGreaterThan(Integer value) {
            addCriterion("lac >", value, "lac");
            return (Criteria) this;
        }

        public Criteria andLacGreaterThanOrEqualTo(Integer value) {
            addCriterion("lac >=", value, "lac");
            return (Criteria) this;
        }

        public Criteria andLacLessThan(Integer value) {
            addCriterion("lac <", value, "lac");
            return (Criteria) this;
        }

        public Criteria andLacLessThanOrEqualTo(Integer value) {
            addCriterion("lac <=", value, "lac");
            return (Criteria) this;
        }

        public Criteria andLacIn(List<Integer> values) {
            addCriterion("lac in", values, "lac");
            return (Criteria) this;
        }

        public Criteria andLacNotIn(List<Integer> values) {
            addCriterion("lac not in", values, "lac");
            return (Criteria) this;
        }

        public Criteria andLacBetween(Integer value1, Integer value2) {
            addCriterion("lac between", value1, value2, "lac");
            return (Criteria) this;
        }

        public Criteria andLacNotBetween(Integer value1, Integer value2) {
            addCriterion("lac not between", value1, value2, "lac");
            return (Criteria) this;
        }

        public Criteria andCellIsNull() {
            addCriterion("cell is null");
            return (Criteria) this;
        }

        public Criteria andCellIsNotNull() {
            addCriterion("cell is not null");
            return (Criteria) this;
        }

        public Criteria andCellEqualTo(Integer value) {
            addCriterion("cell =", value, "cell");
            return (Criteria) this;
        }

        public Criteria andCellNotEqualTo(Integer value) {
            addCriterion("cell <>", value, "cell");
            return (Criteria) this;
        }

        public Criteria andCellGreaterThan(Integer value) {
            addCriterion("cell >", value, "cell");
            return (Criteria) this;
        }

        public Criteria andCellGreaterThanOrEqualTo(Integer value) {
            addCriterion("cell >=", value, "cell");
            return (Criteria) this;
        }

        public Criteria andCellLessThan(Integer value) {
            addCriterion("cell <", value, "cell");
            return (Criteria) this;
        }

        public Criteria andCellLessThanOrEqualTo(Integer value) {
            addCriterion("cell <=", value, "cell");
            return (Criteria) this;
        }

        public Criteria andCellIn(List<Integer> values) {
            addCriterion("cell in", values, "cell");
            return (Criteria) this;
        }

        public Criteria andCellNotIn(List<Integer> values) {
            addCriterion("cell not in", values, "cell");
            return (Criteria) this;
        }

        public Criteria andCellBetween(Integer value1, Integer value2) {
            addCriterion("cell between", value1, value2, "cell");
            return (Criteria) this;
        }

        public Criteria andCellNotBetween(Integer value1, Integer value2) {
            addCriterion("cell not between", value1, value2, "cell");
            return (Criteria) this;
        }

        public Criteria andAccuracyIsNull() {
            addCriterion("accuracy is null");
            return (Criteria) this;
        }

        public Criteria andAccuracyIsNotNull() {
            addCriterion("accuracy is not null");
            return (Criteria) this;
        }

        public Criteria andAccuracyEqualTo(Integer value) {
            addCriterion("accuracy =", value, "accuracy");
            return (Criteria) this;
        }

        public Criteria andAccuracyNotEqualTo(Integer value) {
            addCriterion("accuracy <>", value, "accuracy");
            return (Criteria) this;
        }

        public Criteria andAccuracyGreaterThan(Integer value) {
            addCriterion("accuracy >", value, "accuracy");
            return (Criteria) this;
        }

        public Criteria andAccuracyGreaterThanOrEqualTo(Integer value) {
            addCriterion("accuracy >=", value, "accuracy");
            return (Criteria) this;
        }

        public Criteria andAccuracyLessThan(Integer value) {
            addCriterion("accuracy <", value, "accuracy");
            return (Criteria) this;
        }

        public Criteria andAccuracyLessThanOrEqualTo(Integer value) {
            addCriterion("accuracy <=", value, "accuracy");
            return (Criteria) this;
        }

        public Criteria andAccuracyIn(List<Integer> values) {
            addCriterion("accuracy in", values, "accuracy");
            return (Criteria) this;
        }

        public Criteria andAccuracyNotIn(List<Integer> values) {
            addCriterion("accuracy not in", values, "accuracy");
            return (Criteria) this;
        }

        public Criteria andAccuracyBetween(Integer value1, Integer value2) {
            addCriterion("accuracy between", value1, value2, "accuracy");
            return (Criteria) this;
        }

        public Criteria andAccuracyNotBetween(Integer value1, Integer value2) {
            addCriterion("accuracy not between", value1, value2, "accuracy");
            return (Criteria) this;
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