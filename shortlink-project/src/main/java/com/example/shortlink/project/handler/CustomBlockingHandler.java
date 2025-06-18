package com.example.shortlink.project.handler;


import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.shortlink.project.common.convention.result.Result;
import com.example.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkCreateRespDTO;

public class CustomBlockingHandler {

    public static Result<ShortLinkCreateRespDTO> createShortLinkBlockingHandler(ShortLinkCreateReqDTO requestParam, BlockException exception) {
        return new Result<ShortLinkCreateRespDTO>().setCode("B100000").setMessage("系统繁忙，请稍后再试...");
    }
}
