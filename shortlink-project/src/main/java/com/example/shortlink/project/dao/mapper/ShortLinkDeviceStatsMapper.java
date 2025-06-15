package com.example.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shortlink.project.dao.entity.ShortLinkDeviceStatsDO;
import com.example.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkStatsDeviceRespDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShortLinkDeviceStatsMapper extends BaseMapper<ShortLinkDeviceStatsDO> {

    int insertOrUpdate(@Param("stats")ShortLinkDeviceStatsDO stats);

    List<ShortLinkStatsDeviceRespDTO> getDeviceStats(@Param("param")ShortLinkStatsReqDTO param);
}
