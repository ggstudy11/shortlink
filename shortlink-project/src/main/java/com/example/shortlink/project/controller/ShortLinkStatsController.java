package com.example.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.shortlink.project.common.convention.result.Result;
import com.example.shortlink.project.common.convention.result.Results;
import com.example.shortlink.project.dto.req.ShortLinkAccessReqDTO;
import com.example.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkAccessRespDTO;
import com.example.shortlink.project.dto.resp.ShortLinkStatsRespDTO;
import com.example.shortlink.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/short-link/v1/stats")
@RequiredArgsConstructor
public class ShortLinkStatsController {

    private final ShortLinkStatsService shortLinkStatsService;

    @GetMapping
    public Result<ShortLinkStatsRespDTO> getLinkStats(ShortLinkStatsReqDTO shortLinkStatsReqDTO) {
        return Results.success(shortLinkStatsService.getLinkStats(shortLinkStatsReqDTO));
    }

    @GetMapping("/access")
    public Result<IPage<ShortLinkAccessRespDTO>> pageAccess(ShortLinkAccessReqDTO shortLinkAccessReqDTO) {
        return Results.success(shortLinkStatsService.pageAccess(shortLinkAccessReqDTO));
    }
}
