package com.yunrang.location.datamgr.mysql.dao;

import java.util.ArrayList;
import java.util.List;

import com.yunrang.location.datamgr.mysql.mapper.RepoIpReferDoMapper;
import com.yunrang.location.datamgr.mysql.model.RepoIpReferDo;
import com.yunrang.location.datamgr.mysql.util.UtilMybatisTemplate;
import com.yunrang.location.datamgr.mysql.util.UtilMybatisTemplate.Callback;

public class RepoIpReferDao {
	private static final Class<RepoIpReferDoMapper> mapperClass = RepoIpReferDoMapper.class;
	private UtilMybatisTemplate mybatisTemplate;
	
    public int insertOne(final RepoIpReferDo ipReferDo) throws Exception {
    	mybatisTemplate.execute(mapperClass, new Callback<RepoIpReferDoMapper>() {
			@Override
			public void execute(RepoIpReferDoMapper mapper) {
				mapper.insert(ipReferDo);
			}
        });
    	return ipReferDo.getIpReferId();
    }
    
    public int updateByPrimaryKeySelective(final RepoIpReferDo ipReferDo) throws Exception {
    	final List<Integer> result = new ArrayList<Integer>();
    	mybatisTemplate.execute(mapperClass, new Callback<RepoIpReferDoMapper>() {
			@Override
			public void execute(RepoIpReferDoMapper mapper) {
				int affectNum = mapper.updateByPrimaryKeySelective(ipReferDo);
				result.add(affectNum);
			}
    	});
    	return result.get(0);
    }
    
    public List<RepoIpReferDo> fetchBatchTuples(final int ipReferId) throws Exception {
        final List<RepoIpReferDo> result = new ArrayList<RepoIpReferDo>();
        mybatisTemplate.execute(mapperClass, new Callback<RepoIpReferDoMapper>() {
			@Override
			public void execute(RepoIpReferDoMapper mapper) {
				result.addAll(mapper.selectAscBatchByPrimaryId(ipReferId));
			}
        });
        return result;
    }
    
    public void setMybatisTemplate(UtilMybatisTemplate mybatisTemplate) {
		this.mybatisTemplate = mybatisTemplate;
	}
}
