package com.example.shortlink.project.service.imp;

import com.example.shortlink.project.dao.mapper.ShortLinkAccessStatsMapper;
import com.example.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkBasicStatsRespDTO;
import com.example.shortlink.project.dto.resp.ShortLinkStatsRespDTO;
import com.example.shortlink.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortLinkStatsServiceImpl implements ShortLinkStatsService {

    private final ShortLinkAccessStatsMapper shortLinkAccessStatsMapper;

    @Override
    public ShortLinkStatsRespDTO getLinkStats(ShortLinkStatsReqDTO shortLinkStatsReqDTO) {

        // 获取指定时间段内uv pv uip
        ShortLinkBasicStatsRespDTO shortLinkBasicStatsRespDTO = shortLinkAccessStatsMapper.countBasicStats(shortLinkStatsReqDTO);

        return ShortLinkStatsRespDTO.builder()
                .pv(shortLinkBasicStatsRespDTO.getPv())
                .uv(shortLinkBasicStatsRespDTO.getUv())
                .uip(shortLinkBasicStatsRespDTO.getUip())
                .build();
    }
}
