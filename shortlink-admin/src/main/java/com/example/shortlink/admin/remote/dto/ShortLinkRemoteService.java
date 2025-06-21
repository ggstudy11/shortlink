package com.example.shortlink.admin.remote.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.shortlink.admin.common.convention.result.Result;
import com.example.shortlink.admin.remote.dto.req.*;
import com.example.shortlink.admin.remote.dto.resp.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("project-service")
public interface ShortLinkRemoteService {

    String baseUrl = "http://localhost:8081/api/short-link/v1/link";
    String urlUrl = "http://localhost:8081/api/short-link/v1/url";
    String recycleUrl = "http://localhost:8081/api/short-link/v1/recycle-bin";


    @PostMapping("/api/short-link/v1/link")
    Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO shortLinkCreateReqDTO);

    @GetMapping("/api/short-link/v1/link/page")
    Result<Page<ShortLinkPageRespDTO>> pageShortLink(@SpringQueryMap ShortLinkPageReqDTO shortLinkPageReqDTO);

    @GetMapping("/api/short-link/v1/link/count")
    Result<List<ShortLinkCountRespDTO>> countShortLink(@RequestParam("gids") List<String> gids);
    
    @PostMapping("/api/short-link/v1/link/update")
    Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO shortLinkUpdateReqDTO);

    @GetMapping("/api/short-link/v1/url/title")
    Result<String> getTitle(@RequestParam("url") String url);

    @PostMapping("/api/short-link/v1/recycle-bin")
    Result<Void> removeToBin(@RequestBody ShortLinkToBinReqDTO shortLinkToBinReqDTO);

    @GetMapping("/api/short-link/v1/recycle-bin/page")
    Result<Page<ShortLinkInBinPageRespDTO>> pageInBin(@SpringQueryMap ShortLinkInBinPageReqDTO shortLinkInBinPageReqDTO);

    @PostMapping("/api/short-link/v1/recycle-bin/recover")
    Result<Void> removeFromBin(@RequestBody ShortLinkRmBinReqDTO shortLinkRmBinReqDTO);

    @PostMapping("/api/short-link/v1/recycle-bin/delete")
    Result<Void> deleteShortLink(@RequestBody ShortLinkDeleteReqDTO shortLinkDeleteReqDTO);

    @GetMapping("/api/short-link/v1/stats")
    Result<ShortLinkStatsRespDTO> getLinkStats(@SpringQueryMap ShortLinkStatsReqDTO shortLinkStatsReqDTO);

}
