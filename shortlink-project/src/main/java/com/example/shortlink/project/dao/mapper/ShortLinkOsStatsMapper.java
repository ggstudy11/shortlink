package com.example.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shortlink.project.dao.entity.ShortLinkOsStatsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShortLinkOsStatsMapper extends BaseMapper<ShortLinkOsStatsDO> {

    int insertOrUpdate(@Param("stats") ShortLinkOsStatsDO stats);
}
