package org.heimdall.dal.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.heimdall.dal.dao.model.MchNotify;
import org.heimdall.dal.dao.model.MchNotifyExample;

public interface MchNotifyMapper {
    int countByExample(MchNotifyExample example);

    int deleteByExample(MchNotifyExample example);

    int deleteByPrimaryKey(String orderId);

    int insert(MchNotify record);

    int insertSelective(MchNotify record);

    List<MchNotify> selectByExample(MchNotifyExample example);

    MchNotify selectByPrimaryKey(String orderId);

    int updateByExampleSelective(@Param("record") MchNotify record, @Param("example") MchNotifyExample example);

    int updateByExample(@Param("record") MchNotify record, @Param("example") MchNotifyExample example);

    int updateByPrimaryKeySelective(MchNotify record);

    int updateByPrimaryKey(MchNotify record);

    int insertSelectiveOnDuplicateKeyUpdate(MchNotify record);
}