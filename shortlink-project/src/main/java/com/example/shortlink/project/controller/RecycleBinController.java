package com.example.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.shortlink.project.common.convention.result.Result;
import com.example.shortlink.project.common.convention.result.Results;
import com.example.shortlink.project.dto.req.ShortLinkDeleteReqDTO;
import com.example.shortlink.project.dto.req.ShortLinkInBinPageReqDTO;
import com.example.shortlink.project.dto.req.ShortLinkRmBinReqDTO;
import com.example.shortlink.project.dto.req.ShortLinkToBinReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkInBinPageRespDTO;
import com.example.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/v1/recycle-bin")
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    @PostMapping
    public Result<Void> removeToBin(@RequestBody ShortLinkToBinReqDTO shortLinkToBinReqDTO) {
        recycleBinService.removeToBin(shortLinkToBinReqDTO);
        return Results.success();
    }

    @GetMapping("/page")
    public Result<IPage<ShortLinkInBinPageRespDTO>> pageInBin(ShortLinkInBinPageReqDTO shortLinkInBinPageReqDTO) {
        return Results.success(recycleBinService.pageInBin(shortLinkInBinPageReqDTO));
    }

    @PostMapping("/recover")
    public Result<Void> removeFromBin(@RequestBody ShortLinkRmBinReqDTO shortLinkRmBinReqDTO) {
        recycleBinService.removeFromBin(shortLinkRmBinReqDTO);
        return Results.success();
    }

    @PostMapping("/delete")
    public Result<Void> deleteShortLink(@RequestBody ShortLinkDeleteReqDTO shortLinkDeleteReqDTO) {
        recycleBinService.deleteShortLink(shortLinkDeleteReqDTO);
        return Results.success();
    }
}
