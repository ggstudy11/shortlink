package com.example.shortlink.project.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.shortlink.project.common.convention.result.Result;
import com.example.shortlink.project.common.convention.result.Results;
import com.example.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.example.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.example.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkCountRespDTO;
import com.example.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.example.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.example.shortlink.project.handler.CustomBlockingHandler;
import com.example.shortlink.project.service.ShortLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    @PostMapping("/api/short-link/v1/link")
    @SentinelResource(
            value = "createShortLink",
            blockHandler = "createShortLinkBlockingHandler",
            blockHandlerClass = CustomBlockingHandler.class
    )
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO shortLinkCreateReqDTO) {
        return Results.success(shortLinkService.create(shortLinkCreateReqDTO));
    }

    @GetMapping("/api/short-link/v1/link/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO shortLinkPageReqDTO) {
        return Results.success(shortLinkService.pageShortLink(shortLinkPageReqDTO));
    }

    @GetMapping("/api/short-link/v1/link/count")
    public Result<List<ShortLinkCountRespDTO>> countShortLink(@RequestParam("gids") List<String> gids) {
        return Results.success(shortLinkService.countShortLink(gids));
    }

    @PostMapping("/api/short-link/v1/link/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO shortLinkUpdateReqDTO) {
        shortLinkService.updateShortLink(shortLinkUpdateReqDTO);
        return Results.success();
    }
    
    @GetMapping("/{shortLink}")
    public void restoreShortUri(@PathVariable String shortLink, HttpServletResponse response, HttpServletRequest request) throws IOException {
        shortLinkService.restoreShortUri(shortLink, response, request);
    }
}
