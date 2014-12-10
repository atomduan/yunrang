package com.yunrang.location.datamgr.mysql.dao;


import java.util.ArrayList;
import java.util.List;

import com.yunrang.location.common.bean.ProfilePoi;
import com.yunrang.location.datamgr.mysql.mapper.RepoPoiDoMapper;
import com.yunrang.location.datamgr.mysql.model.RepoPoiDo;
import com.yunrang.location.datamgr.mysql.model.RepoPoiDoCriteria;
import com.yunrang.location.datamgr.mysql.util.UtilBeanToDoConverter;
import com.yunrang.location.datamgr.mysql.util.UtilMybatisTemplate;
import com.yunrang.location.datamgr.mysql.util.UtilMybatisTemplate.Callback;


public class RepoPoiDao {
	private static final Class<RepoPoiDoMapper> mapperClass = RepoPoiDoMapper.class;
	private UtilMybatisTemplate mybatisTemplate;
	
	public int selectFristMatchedPoiId(final String provinceName, final String cityName, final String specAddress) throws Exception {
		final List<RepoPoiDo> resultList = new ArrayList<RepoPoiDo>();
		mybatisTemplate.execute(mapperClass, new Callback<RepoPoiDoMapper>(){
			@Override
			public void execute(RepoPoiDoMapper mapper) {
				RepoPoiDoCriteria repoPoiDoCriteria = new RepoPoiDoCriteria();
				repoPoiDoCriteria.or().andProvinceNameEqualTo(provinceName).andCityNameEqualTo(cityName).andSpecAddressEqualTo(specAddress);
				resultList.addAll(mapper.selectByExample(repoPoiDoCriteria));
			}
		});
		if (resultList.size() > 0) {
			return resultList.get(0).getPoiId();
		} else {
			return -1;
		}
	}
	
	public int selectTupleCount(final String provinceName, final String cityName, final String specAddress) throws Exception {
		final List<Integer> result = new ArrayList<Integer>();
		mybatisTemplate.execute(mapperClass, new Callback<RepoPoiDoMapper>(){
			@Override
			public void execute(RepoPoiDoMapper mapper) {
				RepoPoiDoCriteria repoPoiDoCriteria = new RepoPoiDoCriteria();
				repoPoiDoCriteria.or().andProvinceNameEqualTo(provinceName).andCityNameEqualTo(cityName).andSpecAddressEqualTo(specAddress);
				int r = mapper.countByExample(repoPoiDoCriteria);
				result.add(r);
			}
		});
		return result.get(0);
	}
	
    public int insertSelective(final ProfilePoi bean) throws Exception {
		return insertSelective(UtilBeanToDoConverter.convert2PoiDo(bean));
    }
    
    public int insertSelective(final RepoPoiDo repoPoiDo) throws Exception {
    	mybatisTemplate.execute(mapperClass, new Callback<RepoPoiDoMapper>(){
			@Override
			public void execute(RepoPoiDoMapper mapper) {
				mapper.insertSelective(repoPoiDo);
			}
		});
    	return repoPoiDo.getPoiId();
    }

	public void setMybatisTemplate(UtilMybatisTemplate mybatisTemplate) {
		this.mybatisTemplate = mybatisTemplate;
	}
}