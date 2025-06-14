package com.example.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shortlink.project.dao.entity.ShortLinkAccessStatsDO;
import com.example.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkBasicStatsRespDTO;
import com.example.shortlink.project.dto.resp.ShortLinkHourStatsRespDTO;
import com.example.shortlink.project.dto.resp.ShortLinkStatsAccessDailyRespDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShortLinkAccessStatsMapper extends BaseMapper<ShortLinkAccessStatsDO> {

    int insertOrUpdate(@Param("stats") ShortLinkAccessStatsDO stats);

    ShortLinkBasicStatsRespDTO countBasicStats(@Param("param")ShortLinkStatsReqDTO param);

    List<ShortLinkStatsAccessDailyRespDTO> countStatsDaily(@Param("param") ShortLinkStatsReqDTO param);

    List<ShortLinkHourStatsRespDTO> countStatsHour(@Param("param") ShortLinkStatsReqDTO param);
}
