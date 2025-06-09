package com.example.shortlink.admin.controller;

import com.example.shortlink.admin.common.convention.result.Result;
import com.example.shortlink.admin.remote.dto.ShortLinkRemoteService;
import com.example.shortlink.admin.remote.dto.req.ShortLinkToBinReqDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/short-link/v1/recycle-bin")
public class RecycleBinController {
    public static final ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {};

    @PostMapping
    public Result<Void> removeToBin(@RequestBody ShortLinkToBinReqDTO shortLinkToBinReqDTO) {
        return shortLinkRemoteService.removeToBin(shortLinkToBinReqDTO);
    }
}
