package com.example.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.shortlink.admin.common.convention.result.Result;
import com.example.shortlink.admin.remote.dto.ShortLinkRemoteService;
import com.example.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.example.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.example.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.example.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/v1/link")
public class ShortLinkController {

    private static final ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {};

    @GetMapping("/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO shortLinkPageReqDTO) {
        return shortLinkRemoteService.pageShortLink(shortLinkPageReqDTO);
    }

    @PostMapping
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO shortLinkCreateReqDTO) {
        System.out.println(shortLinkCreateReqDTO);
        return shortLinkRemoteService.createShortLink(shortLinkCreateReqDTO);
    }
}
