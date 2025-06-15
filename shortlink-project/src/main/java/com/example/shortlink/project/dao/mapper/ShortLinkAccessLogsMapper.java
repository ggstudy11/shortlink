package com.example.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shortlink.project.dao.entity.ShortLinkAccessLogsDO;
import com.example.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkStatsTopIpRespDTO;
import com.example.shortlink.project.dto.resp.ShortLinkStatsUvRespDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShortLinkAccessLogsMapper extends BaseMapper<ShortLinkAccessLogsDO> {
    
    List<ShortLinkStatsTopIpRespDTO> getTopIp(@Param("param") ShortLinkStatsReqDTO param);

    List<ShortLinkStatsUvRespDTO> getUvStats(@Param("param") ShortLinkStatsReqDTO param);

    String getUvTypeByUser(@Param("user") String user, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
