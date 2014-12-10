package com.yunrang.location.datamgr.mysql.dao;

import java.util.ArrayList;
import java.util.List;

import com.yunrang.location.common.bean.ProfilePoi;
import com.yunrang.location.datamgr.mysql.mapper.RepoCellStationDoMapper;
import com.yunrang.location.datamgr.mysql.model.RepoCellStationDo;
import com.yunrang.location.datamgr.mysql.model.RepoCellStationDoCriteria;
import com.yunrang.location.datamgr.mysql.util.UtilBeanToDoConverter;
import com.yunrang.location.datamgr.mysql.util.UtilMybatisTemplate;
import com.yunrang.location.datamgr.mysql.util.UtilMybatisTemplate.Callback;


public class RepoCellStationDao {
	private static final Class<RepoCellStationDoMapper> mapperClass = RepoCellStationDoMapper.class;
	private UtilMybatisTemplate mybatisTemplate;
	
	public int updateByPrimaryKeySelective(final ProfilePoi bean) throws Exception {
		return updateByPrimaryKeySelective(UtilBeanToDoConverter.convert2CellStationDo(bean));
	}
	
	public int updateByPrimaryKeySelective(final RepoCellStationDo repoCellStationDo) throws Exception {
		mybatisTemplate.execute(mapperClass, new Callback<RepoCellStationDoMapper>() {
			@Override
			public void execute(RepoCellStationDoMapper mapper) throws Exception {
				mapper.updateByPrimaryKeySelective(repoCellStationDo);
			} 
    	});
		return repoCellStationDo.getCellStationId();
	}
	
	public int selectFristMatchedCellStationId(final Integer mcc, final Integer mnc, final Integer lac, final Integer cell) throws Exception {
		final List<RepoCellStationDo> resultList = new ArrayList<RepoCellStationDo>();
		mybatisTemplate.execute(mapperClass, new Callback<RepoCellStationDoMapper>(){
			@Override
			public void execute(RepoCellStationDoMapper mapper) {
				RepoCellStationDoCriteria repoCellStationDoCriteria = new RepoCellStationDoCriteria();
				repoCellStationDoCriteria.or().andMccEqualTo(mcc).andMncEqualTo(mnc).andLacEqualTo(lac).andCellEqualTo(cell);
				resultList.addAll(mapper.selectByExample(repoCellStationDoCriteria));
			}
		});
		if (resultList.size() > 0) {
			return resultList.get(0).getCellStationId();
		} else {
			return -1;
		}
	}
	
    public int insertSelective (final ProfilePoi bean) throws Exception {
		return insertSelective(UtilBeanToDoConverter.convert2CellStationDo(bean));
    }
    
    public int insertSelective (final RepoCellStationDo repoCellStationDo) throws Exception {
    	mybatisTemplate.execute(mapperClass, new Callback<RepoCellStationDoMapper>() {
			@Override
			public void execute(RepoCellStationDoMapper mapper) {
				mapper.insertSelective(repoCellStationDo);
			} 
    	});
    	return repoCellStationDo.getCellStationId();
    }
    
    public void setMybatisTemplate(UtilMybatisTemplate mybatisTemplate) {
		this.mybatisTemplate = mybatisTemplate;
	}
}