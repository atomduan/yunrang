package com.yunrang.location.datamgr.mysql.dao;

import java.util.ArrayList;
import java.util.List;

import com.yunrang.location.common.bean.ProfilePoi;
import com.yunrang.location.datamgr.mysql.mapper.RepoGeoDoMapper;
import com.yunrang.location.datamgr.mysql.model.RepoGeoDo;
import com.yunrang.location.datamgr.mysql.model.RepoGeoDoCriteria;
import com.yunrang.location.datamgr.mysql.util.UtilBeanToDoConverter;
import com.yunrang.location.datamgr.mysql.util.UtilMybatisTemplate;
import com.yunrang.location.datamgr.mysql.util.UtilMybatisTemplate.Callback;


public class RepoGeoDao {
	private static final Class<RepoGeoDoMapper> mapperClass = RepoGeoDoMapper.class;
	private UtilMybatisTemplate mybatisTemplate;
	
	public int selectFristMatchedGeoId(final Double originLongitude, final Double originLatitude, final Double adjustLongitude, final Double adjustLatitude) throws Exception {
		final List<RepoGeoDo> resultList = new ArrayList<RepoGeoDo>();
		mybatisTemplate.execute(mapperClass, new Callback<RepoGeoDoMapper>(){
			@Override
			public void execute(RepoGeoDoMapper mapper) {
				RepoGeoDoCriteria repoGeoDoCriteria = new RepoGeoDoCriteria();
				repoGeoDoCriteria.or()
					.andOriginLongitudeEqualTo(originLongitude)
					.andOriginLatitudeEqualTo(originLatitude)
					.andAdjustLongitudeEqualTo(adjustLongitude)
					.andAdjustLatitudeEqualTo(adjustLatitude);
				resultList.addAll(mapper.selectByExample(repoGeoDoCriteria));
			}
		});
		if (resultList.size() > 0) {
			return resultList.get(0).getGeoId();
		} else {
			return -1;
		}
	}
	
    public int insertSelective (final ProfilePoi bean) throws Exception {
		return insertSelective(UtilBeanToDoConverter.convert2GeoDo(bean));
    }
    
    public int insertSelective (final RepoGeoDo repoGeoDo) throws Exception {
        mybatisTemplate.execute(mapperClass, new Callback<RepoGeoDoMapper>(){
			@Override
			public void execute(RepoGeoDoMapper mapper) {
				mapper.insertSelective(repoGeoDo);
			}
        });
        return repoGeoDo.getGeoId();
    }
    
    public void setMybatisTemplate(UtilMybatisTemplate mybatisTemplate) {
		this.mybatisTemplate = mybatisTemplate;
	}
}
