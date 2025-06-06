package com.example.shortlink.admin.remote.dto;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.shortlink.admin.common.convention.result.Result;
import com.example.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.example.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.example.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.example.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;

import java.util.HashMap;
import java.util.Map;

public interface ShortLinkRemoteService {

    String baseUrl = "http://localhost:8081/api/short-link/v1/link";

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
}
