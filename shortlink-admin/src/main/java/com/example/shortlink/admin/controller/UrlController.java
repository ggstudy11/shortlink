package com.example.shortlink.admin.controller;

import com.example.shortlink.admin.common.convention.result.Result;
import com.example.shortlink.admin.remote.dto.ShortLinkRemoteService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final ShortLinkRemoteService shortLinkRemoteService;

    @GetMapping("/api/short-link/admin/v1/url/title")
    public Result<String> getTitle(@RequestParam String url) {
        return shortLinkRemoteService.getTitle(url);
    }
}
