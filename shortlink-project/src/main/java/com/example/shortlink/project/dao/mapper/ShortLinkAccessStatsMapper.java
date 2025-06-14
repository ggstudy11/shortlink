package com.example.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shortlink.project.dao.entity.ShortLinkAccessStatsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShortLinkAccessStatsMapper extends BaseMapper<ShortLinkAccessStatsDO> {

    int insertOrUpdate(@Param("stats") ShortLinkAccessStatsDO stats);
}
