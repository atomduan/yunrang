package com.yunrang.location.datamgr;

import java.util.Date;
import java.util.List;

import com.yunrang.location.common.util.UtilIpConverter;
import com.yunrang.location.datamgr.mysql.dao.RepoCellStationDao;
import com.yunrang.location.datamgr.mysql.dao.RepoGeoDao;
import com.yunrang.location.datamgr.mysql.dao.RepoIpRangeDao;
import com.yunrang.location.datamgr.mysql.dao.RepoIpReferDao;
import com.yunrang.location.datamgr.mysql.dao.RepoPoiDao;
import com.yunrang.location.datamgr.mysql.model.RepoCellStationDo;
import com.yunrang.location.datamgr.mysql.model.RepoGeoDo;
import com.yunrang.location.datamgr.mysql.model.RepoIpRangeDo;
import com.yunrang.location.datamgr.mysql.model.RepoIpReferDo;
import com.yunrang.location.datamgr.mysql.model.RepoPoiDo;

@SuppressWarnings("serial")
public class LocationDatamgrDaoTest extends DatamgrTestBase {
	public void __testRepoCellStationDao() throws Exception {
		int stationId;
		System.out.println("repoCellStationDao test :");
		RepoCellStationDao repoCellStationDao = this.repoCellStationDao;
		stationId = repoCellStationDao.selectFristMatchedCellStationId(460, 1, 57613, 143468015);
		System.out.println("selectFristMatchedCellStationId : " + stationId);
		stationId = repoCellStationDao.updateByPrimaryKeySelective(new RepoCellStationDo() {
			{
				this.setTupleStatus(1);
				this.setCellStationId(3);
			}
		});
		System.out.println("updateByPrimaryKeySelective : " + stationId);
		stationId = repoCellStationDao.insertSelective(new RepoCellStationDo() {
			{
				this.setAccuracy(111111);
				this.setCell(111111);
				this.setLac(123);
				this.setMcc(321);
				this.setMnc(444);
			}
		});
		System.out.println("insertSelective : " + stationId);
	}
	
	public void __testRepoGeoDao() throws Exception {
		int geoId = 0;
		RepoGeoDao repoGeoDao = this.repoGeoDao;
		geoId = repoGeoDao.insertSelective(new RepoGeoDo() {
			{
				this.setAdjustGeoType(333);
				this.setAdjustLongitude(0.12345);
				this.setAdjustLatitude(0.12345);
				this.setOriginGeoType(222);
				this.setOriginLongitude(0.9876);
				this.setOriginLatitude(0.9876);
				this.setPoiId(232);
			}
		});
		System.out.println("insertSelective : " + geoId);
		geoId = repoGeoDao.selectFristMatchedGeoId(0.9876, 0.9876, 0.12345, 0.12345);
		System.out.println("selectFristMatchedGeoId : " + geoId);
	}
	
	public void __testRepoIpRangeDao() throws Exception {
		int ipRangeId = 16;
		RepoIpRangeDao repoIpRangeDao = this.repoIpRangeDao;
		List<RepoIpRangeDo> list = repoIpRangeDao.fetchBatchTuples(ipRangeId);
		System.out.println("fetchBatchTuples size : " + list.size());
		RepoIpRangeDo repoIpRangeDo = list.get(list.size()-1);
		System.out.println("fetchBatchTuples last id : " + repoIpRangeDo.getIpRangeId());
		int dupeCount = repoIpRangeDao
			.fetchDupeTuplesCount(UtilIpConverter.toLongValue("219.148.192.0"), UtilIpConverter.toLongValue("219.148.207.255"));
		System.out.println("dupeCount : " + dupeCount);
		repoIpRangeDo = repoIpRangeDao.fetchMinimurnContainedTuple(UtilIpConverter.toLongValue("219.148.192.0"));
		System.out.println("RepoIpRangeDo : " + repoIpRangeDo.getProvinceName());
		repoIpRangeDo = repoIpRangeDao
			.fetchOneTuple(UtilIpConverter.toLongValue("219.148.192.0"), UtilIpConverter.toLongValue("219.148.207.255"));
		System.out.println("RepoIpRangeDo : " + repoIpRangeDo.getCityName());
		ipRangeId = repoIpRangeDao.insertOneTuple(new RepoIpRangeDo(){
			{
				this.setCityCode("121");
				this.setCityName("testtest");
				this.setDescription("lalalal");
				this.setDistrictCode("333");
				this.setDistrictName("testdistrict");
				this.setIpStrEnd("0.0.0.1");
				this.setIpStrEndNum(1L);
				this.setIpStrStart("0.0.0.1");
				this.setIpStrStartNum(1L);
				this.setIsp("test");
				this.setProvinceCode("00001");
				this.setProvinceName("test");
				this.setReferFullCode("kkkkkk");
				this.setRet("test");
				this.setType("asdfas");
				this.setUpdateTime(new Date());
			}
		});
		System.out.println("generated ip range id : " + ipRangeId);
		int affectNum = repoIpRangeDao.refreshTuple(new RepoIpRangeDo() {
			{
				this.setCityCode("222");
				this.setCityName("222");
				this.setDescription("222");
				this.setDistrictCode("222");
				this.setDistrictName("222");
				this.setIpStrEnd("222");
				this.setIpStrEndNum(1L);
				this.setIpStrStart("222");
				this.setIpStrStartNum(1L);
				this.setIsp("222");
				this.setProvinceCode("222");
				this.setProvinceName("222");
				this.setReferFullCode("222");
				this.setRet("222");
				this.setType("222");
				this.setUpdateTime(new Date());
			}
		});
		System.out.println("affect item num : " + affectNum);
		affectNum = repoIpRangeDao.updateByPrimaryIdSelective(new RepoIpRangeDo() {
			{
				this.setIpRangeId(57622);
				this.setCityCode("111");
				this.setCityName("111");
				this.setDescription("111");
				this.setDistrictCode("111");
				this.setDistrictName("111");
				this.setIpStrEnd("444");
				this.setIpStrEndNum(444L);
				this.setIpStrStart("444");
				this.setIpStrStartNum(444L);
				this.setIsp("444");
				this.setProvinceCode("444");
				this.setProvinceName("444");
				this.setReferFullCode("444");
				this.setRet("444");
				this.setType("444");
				this.setUpdateTime(new Date());
			}
		});
		System.out.println("affect item num : " + affectNum);
	}
	
	public void __testRepoIpReferDao() throws Exception {
		int ipReferId = 78;
		RepoIpReferDao repoIpReferDao = this.repoIpReferDao;
		List<RepoIpReferDo> list = repoIpReferDao.fetchBatchTuples(ipReferId);
		System.out.println("fetchBatchTuples size : " + list.size());
		RepoIpReferDo repoIpReferDo = list.get(list.size() - 1);
		System.out.println("fetchBatchTuples last id : " + repoIpReferDo.getIpReferId());
		/**
		ipReferId = repoIpReferDao.insertOne(new RepoIpReferDo() {
			{
				this.setCityCode("55555");
				this.setCityName("444444");
				this.setDistrictCode("333333");
				this.setDistrictName("999999");
				this.setIpRangeId(123);
				this.setIpStr("asdfasd");
				this.setProvinceCode("asdgg");
				this.setProvinceName("gadgeasdfa");
				this.setTupleStatus(0);
				this.setUpdateTime(new Date());
			}
		});
		System.out.println("insertOne : " + ipReferId);
		**/
		int affectNum = repoIpReferDao.updateByPrimaryKeySelective(new RepoIpReferDo() {
			{
				this.setIpReferId(664340);
				this.setCityCode("11111");
				this.setCityName("11111");
				this.setDistrictCode("111111");
				this.setDistrictName("111111");
				this.setIpRangeId(11111);
				this.setIpStr("1111111");
				this.setProvinceCode("1111111");
				this.setProvinceName("1111111");
				this.setTupleStatus(0);
				this.setUpdateTime(new Date());
			}
		});
		System.out.println("affect num : " + affectNum);
	}
	
	public void __testRepoPoiDao() throws Exception {
		RepoPoiDao repoPoiDao = this.repoPoiDao;
		int poiId = repoPoiDao.insertSelective(new RepoPoiDo() {
			{
				this.setCityCode("11111111");
				this.setCityName("jjjjjjj");
				this.setProvinceCode("222222222");
				this.setProvinceName("pppppppppp");
				this.setRawAddress("adfakjsdfakjsdf");
				this.setSpecAddress("ooooooooo");
				this.setTags("tatgagasdf");
				this.setTupleStatus(0);
			}
		});
		System.out.println("insertSelective : " + poiId);
		int i = repoPoiDao.selectFristMatchedPoiId("pppppppppp", "jjjjjjj", "ooooooooo");
		System.out.println(i);
		int count = repoPoiDao.selectTupleCount("pppppppppp", "jjjjjjj", "ooooooooo");
		System.out.println(count);
	}
}
