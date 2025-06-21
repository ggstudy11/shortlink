package com.example.shortlink.admin.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.shortlink.admin.common.convention.result.Result;
import com.example.shortlink.admin.remote.dto.ShortLinkRemoteService;
import com.example.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.example.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.example.shortlink.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import com.example.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.example.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/admin/v1/link")
public class ShortLinkController {

    private final ShortLinkRemoteService shortLinkRemoteService;

    @GetMapping("/page")
    public Result<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO shortLinkPageReqDTO) {
        return shortLinkRemoteService.pageShortLink(shortLinkPageReqDTO);
    }

    @PostMapping
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO shortLinkCreateReqDTO) {
        System.out.println(shortLinkCreateReqDTO);
        return shortLinkRemoteService.createShortLink(shortLinkCreateReqDTO);
    }

    @PostMapping("/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO shortLinkUpdateReqDTO) {
        return shortLinkRemoteService.updateShortLink(shortLinkUpdateReqDTO);
    }
}
