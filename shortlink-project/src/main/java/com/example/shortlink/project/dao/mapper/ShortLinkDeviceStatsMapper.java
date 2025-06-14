package com.example.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shortlink.project.dao.entity.ShortLinkDeviceStatsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShortLinkDeviceStatsMapper extends BaseMapper<ShortLinkDeviceStatsDO> {

    int insertOrUpdate(@Param("stats")ShortLinkDeviceStatsDO stats);
}
