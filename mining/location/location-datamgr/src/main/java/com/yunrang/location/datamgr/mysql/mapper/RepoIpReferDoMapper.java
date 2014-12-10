package com.yunrang.location.datamgr.mysql.mapper;

import com.yunrang.location.datamgr.mysql.model.RepoIpReferDo;
import com.yunrang.location.datamgr.mysql.model.RepoIpReferDoCriteria;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RepoIpReferDoMapper {
    int countByExample(RepoIpReferDoCriteria example);

    int deleteByExample(RepoIpReferDoCriteria example);

    int deleteByPrimaryKey(Integer ipReferId);

    int insert(RepoIpReferDo record);

    int insertSelective(RepoIpReferDo record);

    List<RepoIpReferDo> selectByExample(RepoIpReferDoCriteria example);

    RepoIpReferDo selectByPrimaryKey(Integer ipReferId);

    int updateByExampleSelective(@Param("record") RepoIpReferDo record, @Param("example") RepoIpReferDoCriteria example);

    int updateByExample(@Param("record") RepoIpReferDo record, @Param("example") RepoIpReferDoCriteria example);

    int updateByPrimaryKeySelective(RepoIpReferDo record);

    int updateByPrimaryKey(RepoIpReferDo record);

	List<RepoIpReferDo> selectAscBatchByPrimaryId(int ipReferId);
}