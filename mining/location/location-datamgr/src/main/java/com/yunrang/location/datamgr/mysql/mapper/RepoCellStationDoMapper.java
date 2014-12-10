package com.yunrang.location.datamgr.mysql.mapper;

import com.yunrang.location.datamgr.mysql.model.RepoCellStationDo;
import com.yunrang.location.datamgr.mysql.model.RepoCellStationDoCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RepoCellStationDoMapper {
    int countByExample(RepoCellStationDoCriteria example);

    int deleteByExample(RepoCellStationDoCriteria example);

    int deleteByPrimaryKey(Integer cellStationId);

    int insert(RepoCellStationDo record);

    int insertSelective(RepoCellStationDo record);

    List<RepoCellStationDo> selectByExample(RepoCellStationDoCriteria example);

    RepoCellStationDo selectByPrimaryKey(Integer cellStationId);

    int updateByExampleSelective(@Param("record") RepoCellStationDo record, @Param("example") RepoCellStationDoCriteria example);

    int updateByExample(@Param("record") RepoCellStationDo record, @Param("example") RepoCellStationDoCriteria example);

    int updateByPrimaryKeySelective(RepoCellStationDo record);

    int updateByPrimaryKey(RepoCellStationDo record);
}