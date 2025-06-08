package com.example.shortlink.project.controller;

import com.example.shortlink.project.common.convention.result.Result;
import com.example.shortlink.project.common.convention.result.Results;
import com.example.shortlink.project.dto.req.ShortLinkToBinReqDTO;
import com.example.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
