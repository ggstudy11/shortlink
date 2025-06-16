package com.example.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.shortlink.project.dto.req.ShortLinkAccessReqDTO;
import com.example.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkAccessRespDTO;
import com.example.shortlink.project.dto.resp.ShortLinkStatsRespDTO;

public interface ShortLinkStatsService {
    ShortLinkStatsRespDTO getLinkStats(ShortLinkStatsReqDTO shortLinkStatsReqDTO);

    IPage<ShortLinkAccessRespDTO> pageAccess(ShortLinkAccessReqDTO shortLinkAccessReqDTO);
}
