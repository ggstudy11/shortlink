package com.example.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shortlink.project.dao.entity.ShortLinkLocaleStatsDO;
import com.example.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkStatsLocaleCNRespDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShortLinkLocaleStatsMapper extends BaseMapper<ShortLinkLocaleStatsDO> {

    int insertOrUpdate(@Param("stats") ShortLinkLocaleStatsDO stats);

    List<ShortLinkStatsLocaleCNRespDTO> countStats(@Param("param") ShortLinkStatsReqDTO param);
}
