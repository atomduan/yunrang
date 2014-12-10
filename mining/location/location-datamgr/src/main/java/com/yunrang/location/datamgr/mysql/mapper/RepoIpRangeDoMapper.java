package com.yunrang.location.datamgr.mysql.mapper;

import com.yunrang.location.datamgr.mysql.model.RepoIpRangeDo;
import com.yunrang.location.datamgr.mysql.model.RepoIpRangeDoCriteria;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RepoIpRangeDoMapper {
    int countByExample(RepoIpRangeDoCriteria example);

    int deleteByExample(RepoIpRangeDoCriteria example);

    int deleteByPrimaryKey(Integer ipRangeId);

    int insert(RepoIpRangeDo record);

    int insertSelective(RepoIpRangeDo record);

    List<RepoIpRangeDo> selectByExample(RepoIpRangeDoCriteria example);

    RepoIpRangeDo selectByPrimaryKey(Integer ipRangeId);

    int updateByExampleSelective(@Param("record") RepoIpRangeDo record, @Param("example") RepoIpRangeDoCriteria example);

    int updateByExample(@Param("record") RepoIpRangeDo record, @Param("example") RepoIpRangeDoCriteria example);

    int updateByPrimaryKeySelective(RepoIpRangeDo record);

    int updateByPrimaryKey(RepoIpRangeDo record);

	List<RepoIpRangeDo> selectAscBatchByPrimaryId(int ipRangeId);

	RepoIpRangeDo selectOneByMinimurnContainedIpStr(Long ipNum);
}