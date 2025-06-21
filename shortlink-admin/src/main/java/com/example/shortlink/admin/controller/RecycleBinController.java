package com.example.shortlink.admin.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.shortlink.admin.common.convention.result.Result;
import com.example.shortlink.admin.remote.dto.ShortLinkRemoteService;
import com.example.shortlink.admin.remote.dto.req.ShortLinkDeleteReqDTO;
import com.example.shortlink.admin.remote.dto.req.ShortLinkInBinPageReqDTO;
import com.example.shortlink.admin.remote.dto.req.ShortLinkRmBinReqDTO;
import com.example.shortlink.admin.remote.dto.req.ShortLinkToBinReqDTO;
import com.example.shortlink.admin.remote.dto.resp.ShortLinkInBinPageRespDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/short-link/admin/v1/recycle-bin")
@RequiredArgsConstructor
public class RecycleBinController {
    
    private final ShortLinkRemoteService shortLinkRemoteService;

    @PostMapping
    public Result<Void> removeToBin(@RequestBody ShortLinkToBinReqDTO shortLinkToBinReqDTO) {
        return shortLinkRemoteService.removeToBin(shortLinkToBinReqDTO);
    }

    @GetMapping("/page")
    public Result<Page<ShortLinkInBinPageRespDTO>> page(ShortLinkInBinPageReqDTO shortLinkInBinPageReqDTO) {
        return shortLinkRemoteService.pageInBin(shortLinkInBinPageReqDTO);
    }

    @PostMapping("/recover")
    public Result<Void> removeFromBin(@RequestBody ShortLinkRmBinReqDTO shortLinkRmBinReqDTO) {
        return shortLinkRemoteService.removeFromBin(shortLinkRmBinReqDTO);
    }

    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody ShortLinkDeleteReqDTO shortLinkDeleteReqDTO) {
        return shortLinkRemoteService.deleteShortLink(shortLinkDeleteReqDTO);
    }

}
