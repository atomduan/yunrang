package com.yunrang.location.datamgr.mysql.dao;

import java.util.ArrayList;
import java.util.List;

import com.yunrang.location.datamgr.mysql.mapper.RepoIpRangeDoMapper;
import com.yunrang.location.datamgr.mysql.model.RepoIpRangeDo;
import com.yunrang.location.datamgr.mysql.model.RepoIpRangeDoCriteria;
import com.yunrang.location.datamgr.mysql.util.UtilMybatisTemplate;
import com.yunrang.location.datamgr.mysql.util.UtilMybatisTemplate.Callback;

public class RepoIpRangeDao {
	private static final Class<RepoIpRangeDoMapper> mapperClass = RepoIpRangeDoMapper.class;
	private UtilMybatisTemplate mybatisTemplate;
	
    public int insertOneTuple(final RepoIpRangeDo ipRangeDo) throws Exception {
    	mybatisTemplate.execute(mapperClass, new Callback<RepoIpRangeDoMapper>(){
			@Override
			public void execute(RepoIpRangeDoMapper mapper) {
				mapper.insert(ipRangeDo);
			}
		});
    	return ipRangeDo.getIpRangeId();
    }
    
    public int refreshTuple(final RepoIpRangeDo ipRangeDo) throws Exception {
    	final List<Integer> result = new ArrayList<Integer>();
    	mybatisTemplate.execute(mapperClass, new Callback<RepoIpRangeDoMapper>(){
			@Override
			public void execute(RepoIpRangeDoMapper mapper) {
				RepoIpRangeDoCriteria RepoIpRangeDoCriteria = new RepoIpRangeDoCriteria();
				RepoIpRangeDoCriteria.or().andIpStrStartNumEqualTo(ipRangeDo.getIpStrStartNum()).andIpStrEndNumEqualTo(ipRangeDo.getIpStrEndNum());
				int affect = mapper.updateByExampleSelective(ipRangeDo, RepoIpRangeDoCriteria);
				result.add(affect);
			}
		});
    	return result.get(0);
    }
    
    public int fetchDupeTuplesCount(final Long ipStrStartNum, final Long ipStrEndNum) throws Exception {
    	final List<Integer> result = new ArrayList<Integer>();
    	mybatisTemplate.execute(mapperClass, new Callback<RepoIpRangeDoMapper>(){
			@Override
			public void execute(RepoIpRangeDoMapper mapper) {
				RepoIpRangeDoCriteria repoIpRangeDoCriteria = new RepoIpRangeDoCriteria();
				repoIpRangeDoCriteria.or().andIpStrStartNumEqualTo(ipStrStartNum).andIpStrEndNumEqualTo(ipStrEndNum);
				int r = mapper.countByExample(repoIpRangeDoCriteria);
				result.add(r);
			}
		});
    	return result.get(0);
    }
    
    public RepoIpRangeDo fetchMinimurnContainedTuple(final Long ipNum) throws Exception {
    	final List<RepoIpRangeDo> result = new ArrayList<RepoIpRangeDo>();
    	mybatisTemplate.execute(mapperClass, new Callback<RepoIpRangeDoMapper>(){
			@Override
			public void execute(RepoIpRangeDoMapper mapper) {
				result.add(mapper.selectOneByMinimurnContainedIpStr(ipNum));
			}
    	});
    	return result.get(0);
    }
    
    public RepoIpRangeDo fetchOneTuple(final Long ipStrStartNum, final Long ipStrEndNum) throws Exception {
    	final List<RepoIpRangeDo> result = new ArrayList<RepoIpRangeDo>();
    	mybatisTemplate.execute(mapperClass, new Callback<RepoIpRangeDoMapper>(){
			@Override
			public void execute(RepoIpRangeDoMapper mapper) {
				RepoIpRangeDoCriteria RepoIpRangeDoCriteria = new RepoIpRangeDoCriteria();
				RepoIpRangeDoCriteria.or().andIpStrStartNumEqualTo(ipStrStartNum).andIpStrEndNumEqualTo(ipStrEndNum);
				result.addAll(mapper.selectByExample(RepoIpRangeDoCriteria));
			}
		});
    	return result.get(0);
    }
    
    public List<RepoIpRangeDo> fetchBatchTuples(final int ipRangeId) throws Exception {
        final List<RepoIpRangeDo> result = new ArrayList<RepoIpRangeDo>();
        mybatisTemplate.execute(mapperClass, new Callback<RepoIpRangeDoMapper>(){
			@Override
			public void execute(RepoIpRangeDoMapper mapper) {
				result.addAll(mapper.selectAscBatchByPrimaryId(ipRangeId));
			}
		});
        return result;
    }
    
    public int updateByPrimaryIdSelective(final RepoIpRangeDo ipRangeDo) throws Exception {
    	final List<Integer> result = new ArrayList<Integer>();
    	mybatisTemplate.execute(mapperClass, new Callback<RepoIpRangeDoMapper>(){
			@Override
			public void execute(RepoIpRangeDoMapper mapper) {
				int ipRangeId = ipRangeDo.getIpRangeId();
				RepoIpRangeDoCriteria repoIpRangeDoCriteria = new RepoIpRangeDoCriteria();
				repoIpRangeDoCriteria.or().andIpRangeIdEqualTo(ipRangeId);
				int affectNum = mapper.updateByExampleSelective(ipRangeDo, repoIpRangeDoCriteria);
				result.add(affectNum);
			}
    	});
    	return result.get(0);
    }
    
    public int updateByPrimaryIdTheReferFullCode(String fullLocationCode, int ipRangeId) throws Exception {
    	RepoIpRangeDo repoIpRangeDo = new RepoIpRangeDo();
    	repoIpRangeDo.setIpRangeId(ipRangeId);
    	repoIpRangeDo.setReferFullCode(fullLocationCode);
    	return updateByPrimaryIdSelective(repoIpRangeDo);
    }
    
    public void setMybatisTemplate(UtilMybatisTemplate mybatisTemplate) {
		this.mybatisTemplate = mybatisTemplate;
	}
}
