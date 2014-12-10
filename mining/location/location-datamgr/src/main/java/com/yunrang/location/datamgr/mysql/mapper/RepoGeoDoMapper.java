package com.yunrang.location.datamgr.mysql.mapper;

import com.yunrang.location.datamgr.mysql.model.RepoGeoDo;
import com.yunrang.location.datamgr.mysql.model.RepoGeoDoCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RepoGeoDoMapper {
    int countByExample(RepoGeoDoCriteria example);

    int deleteByExample(RepoGeoDoCriteria example);

    int deleteByPrimaryKey(Integer geoId);

    int insert(RepoGeoDo record);

    int insertSelective(RepoGeoDo record);

    List<RepoGeoDo> selectByExample(RepoGeoDoCriteria example);

    RepoGeoDo selectByPrimaryKey(Integer geoId);

    int updateByExampleSelective(@Param("record") RepoGeoDo record, @Param("example") RepoGeoDoCriteria example);

    int updateByExample(@Param("record") RepoGeoDo record, @Param("example") RepoGeoDoCriteria example);

    int updateByPrimaryKeySelective(RepoGeoDo record);

    int updateByPrimaryKey(RepoGeoDo record);
}