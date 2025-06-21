package com.example.shortlink.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.shortlink.admin.common.convention.result.Result;
import com.example.shortlink.admin.remote.dto.ShortLinkRemoteService;
import com.example.shortlink.admin.remote.dto.req.ShortLinkAccessReqDTO;
import com.example.shortlink.admin.remote.dto.req.ShortLinkStatsReqDTO;
import com.example.shortlink.admin.remote.dto.resp.ShortLinkAccessRespDTO;
import com.example.shortlink.admin.remote.dto.resp.ShortLinkStatsRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/admin/v1/stats")
public class ShortLinkStatsController {

    private final ShortLinkRemoteService shortLinkRemoteService;

    @GetMapping
    public Result<ShortLinkStatsRespDTO> getLinkStats(ShortLinkStatsReqDTO shortLinkStatsReqDTO) {
        return shortLinkRemoteService.getLinkStats(shortLinkStatsReqDTO);
    }

    @GetMapping("/access")
    public Result<Page<ShortLinkAccessRespDTO>> pageAccess(ShortLinkAccessReqDTO shortLinkAccessReqDTO) {
        return shortLinkRemoteService.pageAccess(shortLinkAccessReqDTO);
    }

}
