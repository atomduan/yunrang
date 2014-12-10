package com.yunrang.location.datamgr.mysql.mapper;

import com.yunrang.location.datamgr.mysql.model.RepoPoiDo;
import com.yunrang.location.datamgr.mysql.model.RepoPoiDoCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RepoPoiDoMapper {
    int countByExample(RepoPoiDoCriteria example);

    int deleteByExample(RepoPoiDoCriteria example);

    int deleteByPrimaryKey(Integer poiId);

    int insert(RepoPoiDo record);

    int insertSelective(RepoPoiDo record);

    List<RepoPoiDo> selectByExample(RepoPoiDoCriteria example);

    RepoPoiDo selectByPrimaryKey(Integer poiId);

    int updateByExampleSelective(@Param("record") RepoPoiDo record, @Param("example") RepoPoiDoCriteria example);

    int updateByExample(@Param("record") RepoPoiDo record, @Param("example") RepoPoiDoCriteria example);

    int updateByPrimaryKeySelective(RepoPoiDo record);

    int updateByPrimaryKey(RepoPoiDo record);
}