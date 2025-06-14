package com.example.shortlink.admin.remote.dto;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.shortlink.admin.common.convention.result.Result;
import com.example.shortlink.admin.remote.dto.req.*;
import com.example.shortlink.admin.remote.dto.resp.ShortLinkCountRespDTO;
import com.example.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.example.shortlink.admin.remote.dto.resp.ShortLinkInBinPageRespDTO;
import com.example.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ShortLinkRemoteService {

    String baseUrl = "http://localhost:8081/api/short-link/v1/link";
    String urlUrl = "http://localhost:8081/api/short-link/v1/url";
    String recycleUrl = "http://localhost:8081/api/short-link/v1/recycle-bin";

    default Result<ShortLinkCreateRespDTO> createShortLink(ShortLinkCreateReqDTO shortLinkCreateReqDTO) {
        String jsonStr = HttpUtil.post(baseUrl, JSON.toJSONString(shortLinkCreateReqDTO));
        return JSON.parseObject(jsonStr, new TypeReference<>(){});
    }

    default Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO shortLinkPageReqDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("gid", shortLinkPageReqDTO.getGid());
        params.put("current", shortLinkPageReqDTO.getCurrent());
        params.put("size", shortLinkPageReqDTO.getSize());
        String jsonStr = HttpUtil.get(baseUrl + "/page", params);
        return JSON.parseObject(jsonStr, new TypeReference<>(){});
    }

    default Result<List<ShortLinkCountRespDTO>> countShortLink(List<String> gids) {
        Map<String, Object> params = new HashMap<>();
        params.put("gids", gids);
        String jsonStr = HttpUtil.get(baseUrl + "/count", params);
        return JSON.parseObject(jsonStr, new TypeReference<>(){});
    }

    default Result<Void> updateShortLink(ShortLinkUpdateReqDTO shortLinkUpdateReqDTO) {
        String jsonStr = HttpUtil.post(baseUrl + "/update", JSON.toJSONString(shortLinkUpdateReqDTO));
        return JSON.parseObject(jsonStr, new TypeReference<>(){});
    }

    default Result<String> getTitle(String url) {
        Map<String, Object> params = new HashMap<>();
        params.put("url", url);
        String jsonStr = HttpUtil.get(urlUrl + "/title", params);
        return JSON.parseObject(jsonStr, new TypeReference<>(){});
    }

    default Result<Void> removeToBin(ShortLinkToBinReqDTO shortLinkToBinReqDTO) {
        String jsonStr = HttpUtil.post(baseUrl, JSON.toJSONString(shortLinkToBinReqDTO));
        return JSON.parseObject(jsonStr, new TypeReference<>(){});
    }

    default Result<IPage<ShortLinkInBinPageRespDTO>> pageInBin(ShortLinkInBinPageReqDTO shortLinkInBinPageReqDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("gid", shortLinkInBinPageReqDTO.getGid());
        params.put("current", shortLinkInBinPageReqDTO.getCurrent());
        params.put("size", shortLinkInBinPageReqDTO.getSize());
        String jsonStr = HttpUtil.get(recycleUrl + "/page", params);
        return JSON.parseObject(jsonStr, new TypeReference<>(){});
    }

    default Result<Void> removeFromBin(ShortLinkRmBinReqDTO shortLinkRmBinReqDTO) {
        String jsonStr = HttpUtil.post(recycleUrl + "/recover", JSON.toJSONString(shortLinkRmBinReqDTO));
        return JSON.parseObject(jsonStr, new TypeReference<>(){});
    }

    default Result<Void> deleteShortLink(ShortLinkDeleteReqDTO shortLinkDeleteReqDTO) {
        String jsonStr = HttpUtil.post(recycleUrl + "/delete", JSON.toJSONString(shortLinkDeleteReqDTO));
        return JSON.parseObject(jsonStr, new TypeReference<>(){});
    }
}
