package com.example.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shortlink.project.dao.entity.ShortLinkBrowserStatsDO;
import com.example.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkStatsBrowserRespDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShortLinkBrowserStatsMapper extends BaseMapper<ShortLinkBrowserStatsDO> {

    int insertOrUpdate(@Param("stats") ShortLinkBrowserStatsDO stats);

    List<ShortLinkStatsBrowserRespDTO> getBrowserStats(@Param("param") ShortLinkStatsReqDTO param);
}
