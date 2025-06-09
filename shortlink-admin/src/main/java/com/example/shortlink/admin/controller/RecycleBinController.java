package com.example.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.shortlink.admin.common.convention.result.Result;
import com.example.shortlink.admin.remote.dto.ShortLinkRemoteService;
import com.example.shortlink.admin.remote.dto.req.ShortLinkInBinPageReqDTO;
import com.example.shortlink.admin.remote.dto.req.ShortLinkRmBinReqDTO;
import com.example.shortlink.admin.remote.dto.req.ShortLinkToBinReqDTO;
import com.example.shortlink.admin.remote.dto.resp.ShortLinkInBinPageRespDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/short-link/v1/recycle-bin")
public class RecycleBinController {
    public static final ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {};

    @PostMapping
    public Result<Void> removeToBin(@RequestBody ShortLinkToBinReqDTO shortLinkToBinReqDTO) {
        return shortLinkRemoteService.removeToBin(shortLinkToBinReqDTO);
    }

    @GetMapping("/page")
    public Result<IPage<ShortLinkInBinPageRespDTO>> page(ShortLinkInBinPageReqDTO shortLinkInBinPageReqDTO) {
        return shortLinkRemoteService.pageInBin(shortLinkInBinPageReqDTO);
    }

    @PostMapping("/recover")
    public Result<Void> removeFromBin(@RequestBody ShortLinkRmBinReqDTO shortLinkRmBinReqDTO) {
        return shortLinkRemoteService.removeFromBin(shortLinkRmBinReqDTO);
    }
}
