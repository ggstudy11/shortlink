package com.example.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shortlink.project.dao.entity.ShortLinkOsStatsDO;
import com.example.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkStatsOsRespDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShortLinkOsStatsMapper extends BaseMapper<ShortLinkOsStatsDO> {

    int insertOrUpdate(@Param("stats") ShortLinkOsStatsDO stats);

    List<ShortLinkStatsOsRespDTO> getOsStats(@Param("param") ShortLinkStatsReqDTO param);
}
