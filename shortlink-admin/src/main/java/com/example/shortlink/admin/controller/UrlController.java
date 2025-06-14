package com.example.shortlink.admin.controller;

import com.example.shortlink.admin.common.convention.result.Result;
import com.example.shortlink.admin.remote.dto.ShortLinkRemoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {

    private static final ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {};

    @GetMapping("/api/short-link/v1/url/title")
    public Result<String> getTitle(@RequestParam String url) {
        return shortLinkRemoteService.getTitle(url);
    }
}
